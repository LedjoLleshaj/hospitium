package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findById(long id);
    Optional<Patient> findByCS(String CS);
    Optional<Patient> findByUser(User user);

}
