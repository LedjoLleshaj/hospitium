package it.hospitium.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> findById(long id);
    Optional<Appointment> findByPatient(Patient patient);
    Optional<Appointment> findByMedico(Medico medico);

}
