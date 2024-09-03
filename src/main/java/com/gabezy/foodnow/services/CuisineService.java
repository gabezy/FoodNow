package com.gabezy.foodnow.services;

import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.exceptions.CuisineNotFoundException;
import com.gabezy.foodnow.exceptions.EntityNotFoundException;
import com.gabezy.foodnow.repositories.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public Cuisine findById(Long id) {
        return cuisineRepository.findById(id)
                .orElseThrow(() -> new CuisineNotFoundException(id));
    }
}
