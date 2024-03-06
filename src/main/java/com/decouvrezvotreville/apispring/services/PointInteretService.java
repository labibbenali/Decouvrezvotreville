package com.decouvrezvotreville.apispring.services;

import com.decouvrezvotreville.apispring.requests.PointInteretRequest;
import com.decouvrezvotreville.apispring.requests.PointInteretRequestList;
import com.decouvrezvotreville.apispring.response.PointInteretResponseList;

import java.util.List;

public interface PointInteretService {

     String savePointInteret(PointInteretRequest pointInteret);
     String deletePointInteret(String pointInteret);

     String addPointInteretToUser(PointInteretRequestList pointInteret);

  PointInteretResponseList getAllPointInteret();




}
