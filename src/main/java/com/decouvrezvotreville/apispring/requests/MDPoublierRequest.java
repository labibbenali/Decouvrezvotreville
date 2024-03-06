package com.decouvrezvotreville.apispring.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MDPoublierRequest {
    private String email;
    private String code;
}