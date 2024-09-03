package com.gabezy.foodnow.services;

import com.gabezy.foodnow.domain.entity.City;
import com.gabezy.foodnow.domain.entity.State;
import com.gabezy.foodnow.exceptions.BusinessException;
import com.gabezy.foodnow.exceptions.CityNotFoundException;
import com.gabezy.foodnow.exceptions.StateNotFoundException;
import com.gabezy.foodnow.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final StateService stateService;

    public City save(City input) {
        try {
            State state = stateService.findById(input.getState().getId());
            input.setState(state);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
        return cityRepository.save(input);
    }

    public City findById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException(id));
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City update(Long id, City inupt) {
        var city = findById(id);
        BeanUtils.copyProperties(inupt, city, "id");
        return save(city);
    }

}
