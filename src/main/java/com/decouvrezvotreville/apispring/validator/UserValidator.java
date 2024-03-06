package com.decouvrezvotreville.apispring.validator;

import com.decouvrezvotreville.apispring.entities.User;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {

    public static  List<String> validate(User user){
        List<String> errors = new ArrayList<>();

        if(user == null){
            errors.add("Veuillez renseigner le nom  d'utilisateur");
            errors.add("Veuillez renseigner le prenom  d'utilisateur");
            errors.add("Veuillez renseigner le numero de telephone  d'utilisateur");
            errors.add("Veuillez renseigner le mot de passe  d'utilisateur");
            errors.add("Veuillez renseigner l'adresse  d'utilisateur");
            errors.add("Veuillez renseigner le role  d'utilisateur");
            errors.add("Veuillez renseigner l'adresse mail de l'utilisateur");
            errors.addAll(VilleValidator.validate(user.getVille()));
            return  errors;
        }
        if(!StringUtils.hasLength(user.getNom())){
            errors.add("Veuillez renseigner le nom d'utilisateur");
        }
        if(!StringUtils.hasLength(user.getPrenom())){
            errors.add("Veuillez renseigner le prenom d'utilisateur");
        }
        if(!StringUtils.hasLength(String.valueOf(user.getNumTel()))){
            errors.add("Veuillez renseigner le numero de telephone d'utilisateur");
        }
        if(!StringUtils.hasLength(user.getPassword())){
            errors.add("Veuillez renseigner le mot de passe d'utilisateur");
        }
        if(!StringUtils.hasLength(user.getAdresse())){
            errors.add("Veuillez renseigner l'adresse d'utilisateur");
        }
        if(!StringUtils.hasLength(user.getRole().name())){
            errors.add("Veuillez renseigner le role d'utilisateur");
        }

        if(!StringUtils.hasLength(user.getMail())){
            errors.add("Veuillez renseigner le mail d'utilisateur");
        }
        errors.addAll(VilleValidator.validate(user.getVille()));
        return errors;

    }
}
