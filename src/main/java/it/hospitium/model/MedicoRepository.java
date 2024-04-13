package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MedicoRepository extends CrudRepository<Medico, Long> {
    Optional<Medico> findById(long id);
    Optional<Medico> findByUser(User user);
}
