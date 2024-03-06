package com.decouvrezvotreville.apispring.services.impl;

import com.decouvrezvotreville.apispring.email.EmailSender;
import com.decouvrezvotreville.apispring.entities.MotDePasseOublier;
import com.decouvrezvotreville.apispring.entities.PointInteret;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.entities.Ville;
import com.decouvrezvotreville.apispring.exception.EntityNotFoundException;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.repository.ConfirmationTokenRepository;

import com.decouvrezvotreville.apispring.repository.MotDePasseOublierRespository;
import com.decouvrezvotreville.apispring.repository.UserRepository;

import com.decouvrezvotreville.apispring.repository.VilleRepository;
import com.decouvrezvotreville.apispring.requests.ActivateDesactivateAccountRequest;
import com.decouvrezvotreville.apispring.requests.MDPoublierRequest;
import com.decouvrezvotreville.apispring.requests.UpdateMDPRequest;
import com.decouvrezvotreville.apispring.response.PointInteretResponse;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.response.UserResponse;
import com.decouvrezvotreville.apispring.response.VilleResponse;
import com.decouvrezvotreville.apispring.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private VilleRepository villeRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSender emailSender;
    private final MotDePasseOublierRespository motDePasseOublierRespository;


    @Override
    public StringResponse activateDesactivateUser(ActivateDesactivateAccountRequest activateDesactivateAccountRequest) {
        Optional<User> oldUser = userRepository.findByMail(activateDesactivateAccountRequest.getEmail());
        if( ! oldUser.isPresent()){
            throw  new EntityNotFoundException("Aucun utilisateur existe avec cette adresse mail",
                    ErrorCodes.USER_NOT_FOUND, List.of("USER NOT FOUND"));
        }
        oldUser.get().setActif(activateDesactivateAccountRequest.isActif());
        oldUser.get().setDesactiverByAdmin(!activateDesactivateAccountRequest.isActif());
        userRepository.save(oldUser.get());
        return new StringResponse("Le statu d'utilisateur est bien modifié");
    }

    @Override
    public void sendEmailMDpOublier(String email) {
        emailSender.storeCodeVerification(email);
    }

    @Override
    public String verifyCode(MDPoublierRequest mdPoublierRequest) {
        if (motDePasseOublierRespository.findByMail(mdPoublierRequest.getEmail()).isPresent()){
            MotDePasseOublier mdp = motDePasseOublierRespository.findByMail(mdPoublierRequest.getEmail()).get();
            if(String.valueOf(mdp.getCode()).equals(mdPoublierRequest.getCode())){
                return "Code est bien vérifié";
            }
            throw new EntityNotFoundException("Code est incorrect",ErrorCodes.CODE_NOT_VALID,List.of("CODE NOT VALID"));
        }

        throw new EntityNotFoundException("Aucun Utilisateur associé à ce mail, Veuillez vérifier votre mail ou s'inscrire"
                , ErrorCodes.USER_NOT_FOUND);
    }

    @Override
    public String updateMDP(UpdateMDPRequest updateMDP) {
        if(userRepository.findByMail(updateMDP.getEmail()).isPresent()){
            User user = userRepository.findByMail(updateMDP.getEmail()).get();
            user.setMotDePasse(passwordEncoder.encode(updateMDP.getMotDePasse()));
            userRepository.save(user);

            motDePasseOublierRespository.findByMail(updateMDP.getEmail()).ifPresent(motDePasseOublierRespository::delete);
            return "Mot de passe est bien modifié";
        }
        throw new EntityNotFoundException("Aucun Utilisateur associé à ce mail, Veuillez vérifier votre mail ou s'inscrire"
                , ErrorCodes.USER_NOT_FOUND);
    }

    @Override
    public String updateUser(User user) {
        Optional<User> oldUser = userRepository.findByMail(user.getMail());
        if( ! oldUser.isPresent()){
            throw  new EntityNotFoundException("Aucun utilisateur existe avec cette adresse mail",
                    ErrorCodes.USER_NOT_FOUND, List.of("USER NOT FOUND"));
        }
        User newUser = oldUser.get();
       Ville ville = villeRepository.findById(newUser.getVille().getId()).get();

        ville.setCodeInsee(user.getVille().getCodeInsee());
        ville.setNom(user.getVille().getNom());
        ville.setCodePostale(user.getVille().getCodePostale());
        ville.setDernierModification(LocalDateTime.now());

        villeRepository.save(ville);

        newUser.setNom(user.getNom());
        newUser.setPrenom(user.getPrenom());
        newUser.setAdresse(user.getAdresse());
        newUser.setMail(user.getMail());
        newUser.setDateNaissance(user.getDateNaissance());
        newUser.setNumTel(user.getNumTel());
        newUser.setRole(user.getRole());
        newUser.setMotDePasse(passwordEncoder.encode(user.getPassword()));
        newUser.setVille(ville);
        newUser.setDernierModification(LocalDateTime.now());

        userRepository.save(newUser);

        return "Compte utilisteur est modifié";
    }

    @Override
    public void deleteUtilisateur(String email) {
        if(email == null || email.isEmpty()){
            LOG.error("Utilisateur email is null");
            throw  new EntityNotFoundException("Aucucn utilisateur avec ce mail",ErrorCodes.USER_NOT_FOUND,
                    List.of("USER NOT FOUND"));
        }
        User user = userRepository.findByMail(email).get();

        confirmationTokenRepository.deleteByUser(email);
        userRepository.deleteUserByMail(email);
        villeRepository.deleteById(user.getVille().getId());

    }

    @Override
    public UserResponse getUser(String email) {
        if( ! userRepository.findByMail(email).isPresent()){
            throw new EntityNotFoundException("Aucn utilisateur avec ce mail",ErrorCodes.USER_NOT_FOUND,List.of("USER NOT FOUND"));
        }
        User user = userRepository.findByMail(email).get();
        return mapUserToUserResponse(user);
//        UserResponse userResponse = new UserResponse();
//        VilleResponse villeResponse = new VilleResponse();
//        List<PointInteretResponse> pointInteretResponse = new ArrayList<>();
//        if(! user.getPointInteret().isEmpty()){
//            for(PointInteret pt : user.getPointInteret()){
//                PointInteretResponse ptResponse =  new PointInteretResponse();
//                ptResponse.setPointInteret(pt.getPointInteret());
//               pointInteretResponse.add(ptResponse);
//            }
//
//        }
//        villeResponse.setNom(user.getVille().getNom());
//        villeResponse.setCodePostale(user.getVille().getCodePostale());
//
//        userResponse.setNom(user.getNom());
//        userResponse.setPrenom(user.getPrenom());
//        userResponse.setMail(user.getMail());
//        userResponse.setDateNaissance(user.getDateNaissance());
//        userResponse.setAdresse(user.getAdresse());
//        userResponse.setRole(user.getRole());
//        userResponse.setVille(villeResponse);
//        userResponse.setNumTel(String.valueOf(user.getNumTel()));
//        userResponse.setPointInteret(pointInteretResponse);
//
//        return userResponse;
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(UserServiceImpl::mapUserToUserResponse)
                .collect(Collectors.toList());

    }



    private static UserResponse mapUserToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        VilleResponse villeResponse = new VilleResponse();
        List<PointInteretResponse> pointInteretResponse = new ArrayList<>();
        if(! user.getPointInteret().isEmpty()){
            for(PointInteret pt : user.getPointInteret()){
                PointInteretResponse ptResponse =  new PointInteretResponse();
                ptResponse.setPointInteret(pt.getPointInteret());
                pointInteretResponse.add(ptResponse);
            }

        }
        villeResponse.setNom(user.getVille().getNom());
        villeResponse.setCodePostale(user.getVille().getCodePostale());

        userResponse.setNom(user.getNom());
        userResponse.setPrenom(user.getPrenom());
        userResponse.setMail(user.getMail());
        userResponse.setDateNaissance(user.getDateNaissance());
        userResponse.setAdresse(user.getAdresse());
        userResponse.setRole(user.getRole());
        userResponse.setActif(user.getActif());
        userResponse.setDesactiverByAdmin(user.getDesactiverByAdmin());
        userResponse.setVille(villeResponse);
        userResponse.setNumTel(String.valueOf(user.getNumTel()));
        userResponse.setPointInteret(pointInteretResponse);

        return userResponse;
    }
}
