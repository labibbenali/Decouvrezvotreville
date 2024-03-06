package com.decouvrezvotreville.apispring.email;

public interface EmailSender {
    void send(String to , String email, String objet);
    void storeCodeVerification(String mail);
}
