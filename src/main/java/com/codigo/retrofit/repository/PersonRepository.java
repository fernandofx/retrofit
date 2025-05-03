package com.codigo.retrofit.repository;

import com.codigo.retrofit.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByStatus(String status);
    List<PersonEntity> findAllByStatus(String status);
    Optional<PersonEntity> findByNumberDocument(String dni);
}
