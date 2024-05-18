package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends CrudRepository<Child, Long> {
    Optional<Child> findById(long id);
    Optional<Child> findByCS(String CS);
    Optional<Child> findByMedico(Medico medico);
    List<Child> findByParent(Patient parent);
}
