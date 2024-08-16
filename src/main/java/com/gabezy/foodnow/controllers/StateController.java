package com.gabezy.foodnow.controllers;

import com.gabezy.foodnow.domain.entity.City;
import com.gabezy.foodnow.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    private CityRepository cityRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<City>> getStates() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

}
