package com.alexmart.traffic.api.service;

import com.alexmart.traffic.api.domain.model.Owner;
import com.alexmart.traffic.api.domain.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@AllArgsConstructor
@Component
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    public Owner update(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Transactional
    public void delete(Long id) {
        Owner owner = findById(id);
        ownerRepository.delete(owner);
    }
}
