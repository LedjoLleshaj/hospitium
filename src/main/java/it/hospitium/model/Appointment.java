package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import it.hospitium.model.Visita.VisitType;

import javax.persistence.*;

/**
 * Model for an appointment in the hospital.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private VisitType visitType;
    @Column(nullable = false)
    private Integer insertedBy;
    @Column(nullable = false)
    private Integer urgenza;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;


    public Appointment(String data, VisitType visitType, Integer insertedBy,Integer urgenza , Medico medico, Patient patient, Nurse nurse) {
        this.data = data;
        this.visitType = visitType;
        this.insertedBy = insertedBy;
        this.urgenza = urgenza;
        this.medico = medico;
        this.patient = patient;
        this.nurse = nurse;
    }

    @Override
    public String toString() {
        return String.format("Visit{id=%d, date=%s, medico=%s, patient=%s, nurse=%s, type=%s}", id, data, medico.fullName(), patient.fullName(), nurse.fullName(), visitType.toString());
    }

}
