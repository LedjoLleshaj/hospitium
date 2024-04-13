package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SecretaryRepository extends CrudRepository<Secretary, Long> {
    Optional<Secretary> findById(long id);
    Optional<Secretary> findByUser(User user);}
