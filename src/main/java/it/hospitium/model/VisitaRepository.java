package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VisitaRepository extends CrudRepository<Visita, Long> {
    Optional<Visita> findById(long id);
    Optional<Visita> findByPatient(Patient patient);
    Optional<Visita> findByMedico(Medico medico);
    Optional<Visita> findByNurse(Nurse nurse);

}
