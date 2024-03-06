package com.decouvrezvotreville.apispring.Controller;

import com.decouvrezvotreville.apispring.requests.ActivateDesactivateAccountRequest;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.entities.User;
import com.decouvrezvotreville.apispring.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.decouvrezvotreville.apispring.utils.Constants.APP_ROOT;


@RequestMapping(value=APP_ROOT+"/utilisateurs", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = APP_ROOT + "/utilisateurs")
public interface UserController {

    @GetMapping(value = "getUser/{email}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get info user", description = "Recuperation des info ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "get user info", content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)) })

    })
    public ResponseEntity<UserResponse> getUser(@PathVariable String email);
    @Operation(summary = "Suppression d'un utilisateur", description = "Suppression d'un utilisateur ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "Suppression d'un utilisateur",content =  { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<StringResponse> deleteUser(@Parameter(description = "email", required = true) @PathVariable String email);

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update user", description = "Update user ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "utilisateur est bien modifié",content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    public ResponseEntity<StringResponse> updateUser(@Parameter(description = "User", required = true) @RequestBody  User user);


    @GetMapping(value = "getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get all user", description = "get all user ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "get all user", content =  { @Content(mediaType = "application/json",
                  array= @ArraySchema(schema = @Schema(implementation = UserResponse.class))) })

    })

    public ResponseEntity<List<UserResponse>> getAllUser();

    @PostMapping(value="/enabled-desable", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "activer desacativer user", description = "activer desacativer user")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "activer desacativer user",  content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    public ResponseEntity<StringResponse> activateDesactivateUser(@RequestBody ActivateDesactivateAccountRequest activateDesactivateAccountRequest);

}
