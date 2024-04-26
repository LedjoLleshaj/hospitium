package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;


public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> findById(long id);
    List<Appointment> findByPatient(Patient patient);
    Optional<Appointment> findByMedico(Medico medico);

}
