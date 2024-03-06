package com.decouvrezvotreville.apispring.Controller.impl;

import com.decouvrezvotreville.apispring.Controller.UserController;
import com.decouvrezvotreville.apispring.requests.ActivateDesactivateAccountRequest;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.response.UserResponse;
import com.decouvrezvotreville.apispring.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private UserService userService;


    @Override
    public ResponseEntity<UserResponse> getUser(String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }

    @Override
    public ResponseEntity<StringResponse> deleteUser(String email) {
        userService.deleteUtilisateur(email);
        return ResponseEntity.ok(new StringResponse("Utilisateur est bien supprimé"));
    }

    @Override
    public ResponseEntity<StringResponse> updateUser(  User user) {
       return ResponseEntity.ok(new StringResponse(userService.updateUser(user)));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());

    }

    @Override
    public ResponseEntity<StringResponse> activateDesactivateUser(ActivateDesactivateAccountRequest activateDesactivateAccountRequest) {
        return ResponseEntity.ok(userService.activateDesactivateUser(activateDesactivateAccountRequest));
    }
}
