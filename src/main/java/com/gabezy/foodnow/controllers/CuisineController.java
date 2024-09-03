package com.gabezy.foodnow.controllers;

import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.exceptions.EntityNotFoundException;
import com.gabezy.foodnow.repositories.CuisineRepository;
import com.gabezy.foodnow.services.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cuisines")
@RequiredArgsConstructor
public class CuisineController {

    private final CuisineRepository cuisineRepository;
    private final CuisineService cuisineService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cuisine>> getAll() {
        var cuisines = cuisineRepository.findAll();
        return ResponseEntity.ok(cuisines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuisine> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cuisineService.findById(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<Cuisine>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(cuisineRepository.findByNameContainingIgnoreCase(name, Limit.unlimited()));
    }

    @GetMapping("/like")
    public ResponseEntity<List<Cuisine>> findByNameLike(@RequestParam String name) {
        return ResponseEntity.ok(cuisineRepository.findByNameLike(name));
    }

    @GetMapping("/containing")
    public ResponseEntity<List<Cuisine>> findByNameContaining(@RequestParam String name) {
        return ResponseEntity.ok(cuisineRepository.findByNameContainingIgnoreCase(name));
    }

    @PostMapping
    public ResponseEntity<Cuisine> create(@RequestBody Cuisine cuisine, UriComponentsBuilder builder) {
        var cuisineSaved = cuisineRepository.save(cuisine);
        URI uri = builder.path("/cuisines/{id}").buildAndExpand(cuisineSaved.getId()).toUri();
        return ResponseEntity.created(uri).body(cuisineSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuisine> update(@PathVariable Long id, @RequestBody Cuisine cuisine) {
        var cuisineSaved = cuisineService.findById(id);
        BeanUtils.copyProperties(cuisine, cuisineSaved, "id");
        return ResponseEntity.ok(cuisineRepository.save(cuisineSaved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
            cuisineRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }
}
