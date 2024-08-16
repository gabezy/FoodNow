package com.gabezy.foodnow.infra.repositories;

import com.gabezy.foodnow.repositories.GenericRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

public class GenericRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
    implements GenericRepository<T, ID> {

    private EntityManager manager;

    public GenericRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.manager = entityManager;
    }

    @Override
    public Optional<T> findFirst() {
        var jpql = "from " + getDomainClass().getName();
        T entity = this.manager.createQuery(jpql, getDomainClass())
                .setMaxResults(1)
                .getSingleResult();
        return Optional.ofNullable(entity); // pode ter um valor nulo
    }
}
