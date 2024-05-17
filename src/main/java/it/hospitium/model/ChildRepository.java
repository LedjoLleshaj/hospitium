package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends CrudRepository<Child, Long> {
    Optional<Child> findById(long id);
    Optional<Child> findByCS(String CS);
    Optional<Child> findByUser(User user);
    Optional<Child> findByMedico(Medico medico);
    Optional<Medico> findByMedico_Id(long id);
    List<Child> findByPatient(Patient patient);
}
