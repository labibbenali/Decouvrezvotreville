package com.decouvrezvotreville.apispring.handlers;

import com.decouvrezvotreville.apispring.exception.ErrorCodes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Holds error code , error message and related error messages of an error")
public class ErrorDto {
    @Schema(description = "The error code.", required = true)
    private Integer httpCode;

    @Schema(description = "The error code.", required = true)
    private ErrorCodes code;

    @Schema(description = "A detailed error code.")
    private String message;

    @Schema(description = "The input fields related to the error, if any.")
    private List<String> errors = new ArrayList<>();

}
