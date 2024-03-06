package com.decouvrezvotreville.apispring.requests;

import com.decouvrezvotreville.apispring.entities.PointInteret;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointInteretRequestList {
    List<PointInteret> pointInteret;
    String email;
}


