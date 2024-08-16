package com.gabezy.foodnow.repositories;

import com.gabezy.foodnow.domain.entity.Cuisine;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    List<Cuisine> findByName(String name);
    List<Cuisine> findByNameLike(String name);
    List<Cuisine> findByNameContainingIgnoreCase(String name);
    List<Cuisine> findByNameContainingIgnoreCase(String name, Limit limit);
}