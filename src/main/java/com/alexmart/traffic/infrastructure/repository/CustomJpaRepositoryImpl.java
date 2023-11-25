package com.alexmart.traffic.infrastructure.repository;

import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import com.alexmart.traffic.domain.repository.CustomJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    private final EntityManager manager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.manager = entityManager;
    }

    @Override
    public T findByIdOrFail(ID id) {
//        T entity = findById(id).orElse(null);
        T entity = manager.find(getDomainClass(), id);

        if (entity == null) {
            throw new EntityNotFoundException("Entity '" + getDomainClass().getSimpleName() + "' with 'id " + id + "' not found");
        }

        return entity;
    }
}
