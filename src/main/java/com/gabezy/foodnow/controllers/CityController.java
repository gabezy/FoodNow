package com.gabezy.foodnow.controllers;

import com.gabezy.foodnow.domain.entity.City;
import com.gabezy.foodnow.services.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<City> create(@Valid @RequestBody City input, UriComponentsBuilder builder) {
        var city = cityService.save(input);
        var uri = builder.path("/cities/{id}").buildAndExpand(city.getId()).toUri();
        return ResponseEntity.created(uri).body(city);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<City>> findAll() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @PutMapping("{id}")
    public ResponseEntity<City> update(@PathVariable Long id, @RequestBody City input) {
        return ResponseEntity.ok(cityService.update(id, input));
    }



}
