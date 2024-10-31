package com.gabezy.foodnow.services;

import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.exceptions.CuisineNotFoundException;
import com.gabezy.foodnow.repositories.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CuisineService {

    private final CuisineRepository repository;

    public Cuisine save(Cuisine cuisine) {
        return repository.save(cuisine);
    }

    public Cuisine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException(id));
    }

    public void deleteById(Long id) {
        var cuisine = findById(id);
        repository.delete(cuisine);
    }
}
