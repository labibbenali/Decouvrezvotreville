package com.decouvrezvotreville.apispring.validator;

import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.entities.Ville;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VilleValidator {
    public static  List<String> validate(Ville ville) {
        List<String> errors = new ArrayList<>();
        if(ville == null){
            errors.add("Veuillez renseigner le code insee ");
            errors.add("Veuillez renseigner le nom de la ville ");
            errors.add("Veuillez renseigner le code postal de la ville ");
            return  errors;
        }
        if(!StringUtils.hasLength(ville.getCodeInsee())){
            errors.add("Veuillez renseigner le code insee ");
        }
        if(!StringUtils.hasLength(ville.getNom())){
            errors.add("Veuillez renseigner le nom de la ville ");
        }
        if(!StringUtils.hasLength(ville.getCodePostale())){
            errors.add("Veuillez renseigner le code postal de la ville ");
        }
        return errors;
    }
}
