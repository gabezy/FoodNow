package com.gabezy.foodnow.services;

import com.gabezy.foodnow.domain.entity.State;
import com.gabezy.foodnow.exceptions.StateNotFoundException;
import com.gabezy.foodnow.repositories.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StateService {

    private final StateRepository stateRepository;

    public State findById(Long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new StateNotFoundException(id));
    }

    public State update(Long id, State input) {
        var state = findById(id);
        BeanUtils.copyProperties(input, state, "id");
        return stateRepository.save(state);
    }
}
