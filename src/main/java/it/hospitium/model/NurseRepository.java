package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NurseRepository extends CrudRepository<Nurse, Long> {
    Optional<Nurse> findById(long id);
    Optional<Nurse> findByUser(User user);
    Optional<Nurse> findByMedico(Medico medico);
}
