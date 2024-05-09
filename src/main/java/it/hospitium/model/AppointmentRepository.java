package it.hospitium.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;


public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> findById(long id);
    List<Appointment> findByPatient(Patient patient);
    Optional<Appointment> findByMedico(Medico medico);
    @Query("SELECT a.data FROM Appointment a WHERE a.medico = ?1")
    List<String> findDataByMedico(Medico medico);

}
