package com.decouvrezvotreville.apispring.auth;

import com.decouvrezvotreville.apispring.config.JwtService;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.exception.EntityNotFoundException;
import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import com.decouvrezvotreville.apispring.exception.InvalidEntityException;
import com.decouvrezvotreville.apispring.requests.AuthenticationRequest;
import com.decouvrezvotreville.apispring.requests.MDPoublierRequest;
import com.decouvrezvotreville.apispring.requests.UpdateMDPRequest;
import com.decouvrezvotreville.apispring.response.AuthenticationResponse;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.response.UserResponse;
import com.decouvrezvotreville.apispring.services.AuthenticationService;
import com.decouvrezvotreville.apispring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.decouvrezvotreville.apispring.utils.Constants.AUTHENTICATION_ENDPOINT;

@Controller
@RequestMapping(value = AUTHENTICATION_ENDPOINT,  produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = AUTHENTICATION_ENDPOINT)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;


    private final JwtService jwtService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "inscription ", description = "methode pour l'inscription d'utilisateur ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "utilisateur est bien enregistré")


    })
    public ResponseEntity<StringResponse> register(
            @RequestBody User userRequest){
        return  ResponseEntity.ok(new StringResponse(service.register(userRequest)));

    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "authentificationd d'utilisateur", description = "authentificationd d'utilisateur")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "utilisateur authentifié",content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponse.class)) })
    })
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request){
        return  ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping(path="/confirm")
    public String confirm(Model model,@RequestParam("token") String token){
        return service.confirmToken(token);

    }


    @PostMapping(value = "/getUsername", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get user by token", description = "get user by token")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "utilisateur est trouvé",content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)) })
    })
    public ResponseEntity<UserResponse> getUserFromToken(@RequestBody Map<String, String> token) {
        // La logique pour extraire le nom d'utilisateur du token
        String username = jwtService.extractUsername(token.get("token"));

        if (username != null) {
            return ResponseEntity.ok(userService.getUser(username));
        } else {
            throw new InvalidEntityException("Invalid token, no user associate to this token", ErrorCodes.USER_NOT_FOUND);
        }
    }

    @PostMapping(value = "/verifyemail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "verify email", description = "verify email")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "verification email",content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })
    })
    public ResponseEntity<StringResponse> verifyUserExist(@RequestBody Map<String, String> email){
        if(this.userService.getUser(email.get("email")) != null){
            this.userService.sendEmailMDpOublier(email.get("email"));
            return ResponseEntity.ok(new StringResponse("user exist"));
        }
        throw new EntityNotFoundException("Aucun Utilisateur associé à ce mail, Veuillez vérifier votre mail ou s'inscrire"
                , ErrorCodes.USER_NOT_FOUND);
    }

    @PostMapping(value = "/verifyecode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "verify code", description = "verify code")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "verification code",content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })
    })
    public ResponseEntity<StringResponse> verifyCode(@RequestBody MDPoublierRequest mdPoublierRequest){
        if(this.userService.getUser(mdPoublierRequest.getEmail()) != null){
            return ResponseEntity.ok( new StringResponse(this.userService.verifyCode(mdPoublierRequest)));
        }
        throw new EntityNotFoundException("Aucun Utilisateur associé à ce mail, Veuillez vérifier votre mail ou s'inscrire"
                , ErrorCodes.USER_NOT_FOUND);
    }

    @PostMapping(value = "/update-mdp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update mdp", description = "update mdp")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "update mdp",content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })
    })
    public ResponseEntity<StringResponse> updateMdp(@RequestBody UpdateMDPRequest updateMDP){
        if(this.userService.getUser(updateMDP.getEmail()) != null){
            return ResponseEntity.ok( new StringResponse(this.userService.updateMDP(updateMDP)));
        }
        throw new EntityNotFoundException("Aucun Utilisateur associé à ce mail, Veuillez vérifier votre mail ou s'inscrire"
                , ErrorCodes.USER_NOT_FOUND);
    }

}
