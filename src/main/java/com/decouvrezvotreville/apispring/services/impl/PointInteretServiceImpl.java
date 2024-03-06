package com.decouvrezvotreville.apispring.services.impl;
import com.decouvrezvotreville.apispring.entities.PointInteret;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.exception.EntityNotFoundException;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.exception.InvalidEntityException;
import com.decouvrezvotreville.apispring.repository.PointInteretRepository;
import com.decouvrezvotreville.apispring.repository.UserRepository;
import com.decouvrezvotreville.apispring.requests.PointInteretRequest;
import com.decouvrezvotreville.apispring.requests.PointInteretRequestList;
import com.decouvrezvotreville.apispring.response.PointInteretResponseList;
import com.decouvrezvotreville.apispring.services.PointInteretService;
import com.decouvrezvotreville.apispring.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PointInteretServiceImpl implements PointInteretService {
    private  PointInteretRepository pointInteretRepository;
    private  UserService userService;
    private  UserRepository userRepository;

    @Override
    public String savePointInteret(PointInteretRequest pointInteret) {
        if(pointInteret.getPointInteret().getPointInteret().isEmpty()){
            throw new InvalidEntityException("Point d'interet invalid");
        }
         pointInteret.getPointInteret().setDateCreation(LocalDateTime.now());
        pointInteretRepository.save(pointInteret.getPointInteret());

        return "Point interet est bien enregistré";
    }

    @Override
    public String deletePointInteret(String pointInteret) {
        if(pointInteret == null || pointInteret.isEmpty()){
            throw new InvalidEntityException("Veuillez renseigné l pint d'interet",
                    ErrorCodes.POINTINTERET_NOT_FOUND);
        }
        pointInteretRepository.deleteById(pointInteret);
        return "Point d'ineteret a bien supprimé";
    }

    @Override
    public String addPointInteretToUser(PointInteretRequestList pointInteret) {
        String email = pointInteret.getEmail();
        if( ! userRepository.findByMail(email).isPresent()){
            throw new EntityNotFoundException("Aucun Utilisateur avec ce mail", ErrorCodes.USER_NOT_FOUND);
        }

        User user = userRepository.findByMail(email).get();

        for(PointInteret pointInteret1 : pointInteret.getPointInteret()){
            PointInteret pointInteretInBDD = pointInteretRepository.findById(pointInteret1.getPointInteret()).orElseThrow(
                    ()->{
                        throw new InvalidEntityException("Aucun point d'interet avec ce nom :"+pointInteret1.getPointInteret(),
                                ErrorCodes.POINTINTERET_NOT_FOUND);
                    }
            );
            }

        user.getPointInteret().clear();
        userRepository.save(user);
        for(PointInteret pointInteret1 : pointInteret.getPointInteret()){
            PointInteret pointInteretInBDD = pointInteretRepository.findById(pointInteret1.getPointInteret()).get();
            user.getPointInteret().add(pointInteretInBDD);
            if( ! userRepository.existsByPointInteret(pointInteret1)){
                user.getPointInteret().add(pointInteretInBDD);
                userService.updateUser(user);
            }

        }

        return "Point d'interet est bien ajouté au utilisateur";
    }

    @Override
    public PointInteretResponseList getAllPointInteret() {
        List<PointInteret> pointInterets = pointInteretRepository.findAll();
        if(pointInterets.isEmpty()){
            throw new EntityNotFoundException("Aucun point d'interet trouvé", ErrorCodes.POINTINTERET_NOT_FOUND);
        }
        List<String> pointInteretList = new ArrayList<>();
        for(PointInteret pointInteret : pointInterets){
            pointInteretList.add(pointInteret.getPointInteret());
        }
        PointInteretResponseList pointInteretResponseList = new PointInteretResponseList();
        pointInteretResponseList.setPointInteret(pointInteretList);
        return pointInteretResponseList;
    }

}
