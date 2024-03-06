package com.decouvrezvotreville.apispring.services;

import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.requests.ActivateDesactivateAccountRequest;
import com.decouvrezvotreville.apispring.requests.MDPoublierRequest;
import com.decouvrezvotreville.apispring.requests.UpdateMDPRequest;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.response.UserResponse;

import java.util.List;

public interface UserService {

     String updateUser(User user);
     void deleteUtilisateur(String email);
     UserResponse getUser(String email);
     List<UserResponse> getAllUser();

     StringResponse activateDesactivateUser(ActivateDesactivateAccountRequest activateDesactivateAccountRequest);
     void sendEmailMDpOublier(String email);

     String verifyCode(MDPoublierRequest mdPoublierRequest);
     String updateMDP(UpdateMDPRequest updateMDP);
}
