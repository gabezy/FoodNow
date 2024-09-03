package com.gabezy.foodnow.controllers;

import com.gabezy.foodnow.domain.entity.State;
import com.gabezy.foodnow.repositories.StateRepository;
import com.gabezy.foodnow.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;
    private final StateRepository stateRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<State>> findAll() {
        return ResponseEntity.ok(stateRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stateService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> update(@PathVariable Long id, @RequestBody State input) {
        return ResponseEntity.ok(stateService.update(id, input));
    }

}
