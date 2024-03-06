package com.decouvrezvotreville.apispring.validator;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
public class EmailValidator {
    public static boolean isMailValid(String email){
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

}
