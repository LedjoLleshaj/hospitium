package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VisitaRepository extends CrudRepository<Visita, Long> {
    Optional<Visita> findById(long id);
    Iterable<Visita> findByPatient(Patient patient);
    Iterable<Visita> findByMedico(Medico medico);
    Iterable<Visita> findByNurse(Nurse nurse);

}
