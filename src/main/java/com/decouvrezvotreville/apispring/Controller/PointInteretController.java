package com.decouvrezvotreville.apispring.Controller;

import com.decouvrezvotreville.apispring.response.PointInteretResponseList;
import com.decouvrezvotreville.apispring.response.StringResponse;
import com.decouvrezvotreville.apispring.requests.PointInteretRequest;
import com.decouvrezvotreville.apispring.requests.PointInteretRequestList;
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

import static com.decouvrezvotreville.apispring.utils.Constants.APP_ROOT;

@RequestMapping(value = APP_ROOT+"/pointinteret")
@Tag(name = APP_ROOT + "/pointinteret")
public interface PointInteretController {
    @PostMapping(value =  "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create point d'interet", description = "Creates un nouveau point d'interet ")
    @ApiResponses(value={

            @ApiResponse(responseCode  = "201", description = "creation d'un point d'interet",content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    public ResponseEntity<StringResponse> createPointInteret(@Parameter(description = "PointInteretRequest", required = true)@RequestBody PointInteretRequest pointInteretRequest);



    @PostMapping(value="/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ajouter point d'interet", description = "Ajouter un nouveau point d'interet ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "ajout d'un point d'interet au utilisateur",  content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    public ResponseEntity<StringResponse> addPointInteretTouser(@Parameter(description = "PointInteretRequestList", required = true) @RequestBody PointInteretRequestList pointInteretRequestList);

    @DeleteMapping("/delete/{email}/{pointInteret}")
    @Operation(summary = "Supprimer un point d'interet", description = "Supprimer un nouveau point d'interet ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "Suppression d'un point d'interet au utilisateur" , content =   { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StringResponse.class)) })


    })
    public ResponseEntity<StringResponse>  supprimerPointInteret(@PathVariable String email,@PathVariable String pointInteret);


    @GetMapping(value = "/getAll",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get list pi", description = "Recuperation les liste de point d'interet ")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "201", description = "get list point interet", content =  { @Content(mediaType = "application/json",
                    array=@ArraySchema(schema = @Schema(implementation = PointInteretResponseList.class))) })

    })
    public ResponseEntity<PointInteretResponseList> getAllPointInteret();

}
