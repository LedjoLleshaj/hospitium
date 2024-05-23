package it.hospitium.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.List;


public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Optional<Appointment> findById(long id);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByMedico(Medico medico);
    List<Appointment> findByNurse(Nurse nurse);
    List<Appointment> findByChild(Child child);
    @Query("SELECT a.data FROM Appointment a WHERE a.medico = ?1")
    List<String> findDataByMedico(Medico medico);
    @Query("SELECT a.data FROM Appointment a WHERE a.nurse = ?1")
    List<String> findDataByNurse(Nurse nurse);
    Void deleteById(long id);

}
