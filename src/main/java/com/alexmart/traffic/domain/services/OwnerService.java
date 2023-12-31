package com.alexmart.traffic.domain.services;

import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.model.Owner;
import com.alexmart.traffic.domain.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    public Owner findById(Long id) {
        return ownerRepository.findByIdOrFail(id);
    }

    @Transactional
    public Owner save(Owner owner) {
        boolean isAnyOwnerWithTheSameEmail = ownerRepository
                .findByEmail(owner.getEmail())
                .filter(o -> !o.equals(owner))
                .isPresent();

        if (isAnyOwnerWithTheSameEmail) {
            throw new BusinessException("Owner with the same email already exists");
        }

        return ownerRepository.save(owner);
    }

    @Transactional
    public Owner update(Owner owner) {
        return save(owner);
    }

    @Transactional
    public void delete(Long id) {
        Owner owner = findById(id);
        ownerRepository.delete(owner);
    }
}
