package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import it.hospitium.model.Visita.VisitType;

import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;

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
    private Integer urgenza;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Appointment(String data, VisitType visitType, Integer urgenza, Medico medico,
            Patient patient) {
        this.data = data;
        this.visitType = visitType;
        this.urgenza = urgenza;
        this.medico = medico;
        this.patient = patient;
    }

    @Override
    public String toString() {
        return String.format("Visit{id=%d, data_di_nascita=%s, medico=%s, patient=%s, type=%s}", id, data,
                medico.fullName(), patient.fullName(), visitType.toString());
    }

}
