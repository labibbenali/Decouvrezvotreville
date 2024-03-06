package com.decouvrezvotreville.apispring.Controller.impl;
import com.decouvrezvotreville.apispring.Controller.PointInteretController;
import com.decouvrezvotreville.apispring.response.PointInteretResponseList;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.exception.EntityNotFoundException;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.exception.InvalidEntityException;
import com.decouvrezvotreville.apispring.repository.UserRepository;
import com.decouvrezvotreville.apispring.requests.PointInteretRequest;
import com.decouvrezvotreville.apispring.requests.PointInteretRequestList;
import com.decouvrezvotreville.apispring.services.PointInteretService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class PointInteretControllerImpl implements PointInteretController {

    private PointInteretService pointInteretService;
    private UserRepository userRepository;

    @Override
    public ResponseEntity<StringResponse> createPointInteret(PointInteretRequest pointInteret) {
      String email = pointInteret.getEmail();
      if( ! userRepository.findByMail(email).isPresent()){
          throw new EntityNotFoundException("Aucun Utilisateur avec ce mail", ErrorCodes.USER_NOT_FOUND);

      }
        String role = userRepository.findByMail(email).get().getRole().name();
      if( ! role.equals("ADMIN")){
          throw new InvalidEntityException("Vous n'êtes pas autorisé à faire cette action",ErrorCodes.ACTION_NOT_AUTHORIZED,
                  List.of("ACTION_NOT_AUTHORIZED"));
      }
        return ResponseEntity.ok(new StringResponse(pointInteretService.savePointInteret(pointInteret)));
    }

    @Override
    public ResponseEntity<StringResponse> addPointInteretTouser(PointInteretRequestList pointInteretRequestList) {
        return ResponseEntity.ok(new StringResponse(pointInteretService.addPointInteretToUser(pointInteretRequestList)));
    }

    @Override
    public ResponseEntity<StringResponse> supprimerPointInteret(String email,String pointInteret) {

        if( ! userRepository.findByMail(email).isPresent()){
            throw new EntityNotFoundException("Aucun Utilisateur avec ce mail", ErrorCodes.USER_NOT_FOUND);

        }
        String role = userRepository.findByMail(email).get().getRole().name();
        if( ! role.equals("ADMIN")){
            throw new InvalidEntityException("Vous n'êtes pas autorisé à faire cette action",ErrorCodes.ACTION_NOT_AUTHORIZED,
                    List.of("ACTION_NOT_AUTHORIZED"));
        }
        return ResponseEntity.ok(new StringResponse(pointInteretService.deletePointInteret(
                pointInteret))
        );
    }

    @Override
    public ResponseEntity<PointInteretResponseList> getAllPointInteret() {
        return ResponseEntity.ok(pointInteretService.getAllPointInteret());
    }
}
