package com.alexmart.traffic.domain.repository;

import com.alexmart.traffic.domain.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OwnerRepository extends CustomJpaRepository<Owner, Long>, JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);
}
