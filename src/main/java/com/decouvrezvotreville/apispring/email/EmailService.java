package com.decouvrezvotreville.apispring.email;

import com.decouvrezvotreville.apispring.entities.MotDePasseOublier;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.exception.VerificationEmailException;
import com.decouvrezvotreville.apispring.repository.MotDePasseOublierRespository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final MotDePasseOublierRespository motDePasseOublierRespository;

    @Override
    @Async
    public void send(String to, String email, String objet) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, "utf-8"
            );
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(objet);
            helper.setFrom("citydiscovery.email@gmail.com");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    public void storeCodeVerification(String mail) {
        //generation nombre of 6 chiffre
        int code = (int) Math.pow(10, 6);
        // Initialisation du générateur aléatoire
        Random random = new Random();
        // Génération du nombre aléatoire
        code = random.nextInt(code);


        if (!mail.isEmpty()) {
            if (motDePasseOublierRespository.findByMail(mail).isPresent()) {
                MotDePasseOublier mdp = motDePasseOublierRespository.findByMail(mail).get();
                mdp.setCode(code);
                motDePasseOublierRespository.save(mdp);
            } else {
                MotDePasseOublier mdp = new MotDePasseOublier();
                mdp.setMail(mail);
                mdp.setCode(code);
                motDePasseOublierRespository.save(mdp);
            }

                try {
                    send(mail, buildPasswordResetEmail(code), "Réinitialisation de mot de passe");
                } catch (Exception e) {
                    throw new VerificationEmailException("Une erreur s'est produite lors de l'envoi du mail de vérification, réessayez plus tard", ErrorCodes.ERREUR_VERIFICATION_EMAIL);
                }


        } else {
            throw new IllegalStateException("mail is invalide");
        }

    }

    private String buildPasswordResetEmail(int verificationCode) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c\">\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "              <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n" +
                "                <tr>\n" +
                "                  <td style=\"padding-left: 10px\"></td>\n" +
                "                  <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px\">\n" +
                "                    <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block\">Réinitialisation de mot de passe</span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100%!important\" width=\"100%\">\n" +
                "    <tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n" +
                "          <tr>\n" +
                "            <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100%!important\" width=\"100%\">\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px\">\n" +
                "        <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Bonjour " + ",</p>" +
                "        <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Vous avez demandé la réinitialisation de votre mot de passe. Utilisez le code de vérification suivant :</p>" +
                "        <blockquote style=\"Margin: 0 0 20px 0; border-left: 10px solid #b1b4b6; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px\">\n" +
                "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">" + verificationCode + "</p>\n" +
                "        </blockquote>\n" +
                "        <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Utilisez ce code pour réinitialiser votre mot de passe. Merci!</p>" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "</div>";
    }


}

