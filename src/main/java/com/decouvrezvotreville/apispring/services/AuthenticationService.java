package com.decouvrezvotreville.apispring.services;

import com.decouvrezvotreville.apispring.requests.AuthenticationRequest;
import com.decouvrezvotreville.apispring.response.AuthenticationResponse;
import com.decouvrezvotreville.apispring.config.JwtService;
import com.decouvrezvotreville.apispring.email.EmailSender;
import com.decouvrezvotreville.apispring.entities.ConfirmationToken;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.entities.Ville;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.exception.InvalidEntityException;
import com.decouvrezvotreville.apispring.exception.VerificationEmailException;
import com.decouvrezvotreville.apispring.repository.UserRepository;
import com.decouvrezvotreville.apispring.repository.VilleRepository;
import com.decouvrezvotreville.apispring.validator.EmailValidator;
import com.decouvrezvotreville.apispring.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    int DUREE_EXPIRATION_MAIL = 30;
    private final UserRepository userRepository;
    private final VilleRepository villeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    public String register(User userRequest) {
        List<String> errors = UserValidator.validate(userRequest);
        //On verifie si il y a des erreurs de validation on léve une excetion
        if(!errors.isEmpty()){
            LOG.error("Utilisateur n'est pas valid {}",userRequest);
            throw new InvalidEntityException(
                    "Utilisateur n'est pas valid ", ErrorCodes.USER_NOT_VALID,errors
            );
        }

        boolean isUserExist = userRepository.findByMail(userRequest.getMail()).isPresent();
        if(isUserExist) {
            LOG.error("Email already taken");
          throw new InvalidEntityException("Email already taken",ErrorCodes.EMAIL_ALREADY_TAKEN,List.of("Email est déjà utilisée"));

        }

        if(!EmailValidator.isMailValid(userRequest.getMail())){
            throw new InvalidEntityException("Email invalid",ErrorCodes.EMAIL_INVALID, List.of("Email n'est pas valide"));
        }
        var user = User.builder()
                .nom(userRequest.getNom())
                .prenom(userRequest.getPrenom())
                .mail(userRequest.getMail())
                .motDePasse(passwordEncoder.encode(userRequest.getMotDePasse()))
                .role(userRequest.getRole())
                .adresse(userRequest.getAdresse())
                .dateCreation(LocalDateTime.now())
                .numTel(userRequest.getNumTel())
                .dateNaissance(userRequest.getDateNaissance())
                .actif(false)
                .desactiverByAdmin(false)
                .build();
        //generation of token
        var jwtToken = jwtService.generateToken(user);
        ConfirmationToken confirmationToken  = new ConfirmationToken(
                jwtToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(DUREE_EXPIRATION_MAIL),
                userRequest
        );

        //send MAil
        sendMail(jwtToken, userRequest);

        Ville ville = userRequest.getVille();
        ville.setDateCreation(LocalDateTime.now());

        ville =villeRepository.save(ville);
        user.setVille(ville);
        //save user in database
        userRepository.save(user);

        //save confirmationToken in BDD
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return "Email d'activation envoyé. Veuillez activer votre compte";
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        if( ! userRepository.findByMail(request.getEmail()).isPresent()){
           throw  new InvalidEntityException("Utilisateur n'est pas trouvé , Veuillez s'inscrire", ErrorCodes.USER_NOT_FOUND,
                   List.of("Utilisateur n'est pas trouvé"));
        }

        User user = userRepository.findByMail(request.getEmail()).get();

        if( ! confirmationTokenService.findTokenByUser(user.getMail()).isPresent()){
            throw new  IllegalStateException("token not found");
        }
        ConfirmationToken confirmationToken = confirmationTokenService.findTokenByUser(user.getMail()).get();

        if(user.getDesactiverByAdmin() && !user.getActif()){
            throw new VerificationEmailException("Compte est désactiver, Veuillez contacter l'administrateur",
                    ErrorCodes.ACCOUNT_IS_DESACTIVATED, List.of("Compte est désactiver"));

        }

        // on verifie si le lien est expiré ou non
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(!user.getActif()  && expiredAt.isBefore(LocalDateTime.now()) && !user.getDesactiverByAdmin() ){
            //si le mail est expiré et le compte n'est pas activé , on change le date d'expiration et on envoie un autre mail
            confirmationTokenService.updateExpiredAt(confirmationToken.getToken(), LocalDateTime.now().plusMinutes(DUREE_EXPIRATION_MAIL));
            sendMail(confirmationToken.getToken(),user);
            throw new VerificationEmailException("Compte n'est pas activé, un email est envoyé , Veuillez activer votre compte",
                    ErrorCodes.ACCOUNT_NOT_ACTIVATED, List.of("Compte n'est pas activé"));
        }

        if(!user.getActif() && ! expiredAt.isBefore(LocalDateTime.now()) && !user.getDesactiverByAdmin()){
                throw new VerificationEmailException("Compte n'est pas activé, Veuillez activer votre compte",
                        ErrorCodes.ACCOUNT_NOT_ACTIVATED, List.of("Compte n'est pas activé"));

        }


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

         var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()->
                        new IllegalStateException("token not found")
                );
        if(confirmationToken.getConfirmedAt() != null){
            return "emailAlreadyConfirmed";
           // throw  new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            return "Emailexpired";
           // throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        userRepository.enableUser(
                confirmationToken.getUser().getMail()
        );
        return "confirmationSuccess";
    }
    //SEnd mail Function
    private void sendMail(String jwtToken, User user ) {
        String link = "http://localhost:8080/api/v1/auth/confirm?token="+jwtToken;
        try{
            emailSender.send(user.getMail(), buildEmail(
                    user.getNom(),link,DUREE_EXPIRATION_MAIL
            ), "Activer votre compte");
        }catch(Exception e){
            throw new VerificationEmailException("Une erreur s'est produite lors de l'envoi du mail de vérification, réessayez plus tard",ErrorCodes.ERREUR_VERIFICATION_EMAIL);
        }
    }
    private String buildEmail(String name, String link,int dureeExpiration) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Activer votre compte</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Bonjour " + name + ",</p>" +
                "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Merci de vous être inscrit. Pour activer votre compte, veuillez cliquer sur le lien ci-dessous :" +
                " </p>" +
                "<blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activer mon compte</a> </p></blockquote>\n Ce lien expirera dans "+dureeExpiration+" minutes. <p>Merci</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
