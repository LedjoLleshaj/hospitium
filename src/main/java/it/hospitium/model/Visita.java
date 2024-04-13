package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Model for a visit at the doctor
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String data;
    @Column(nullable = false)
    private String Result;
    @Column(nullable = false)
    private VisitType visitType;
    @Column(nullable = false)
    private Integer insertedBy;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;

    public Visita(String data, String Result, VisitType visitType, Integer insertedBy, Medico medico, Patient patient, Nurse nurse) {
        this.data = data;
        this.Result = Result;
        this.visitType = visitType;
        this.insertedBy = insertedBy;
        this.medico = medico;
        this.patient = patient;
        this.nurse = nurse;
    }

    @Override
    public String toString() {
        return String.format("Visit{id=%d, date=%s, medico=%s, patient=%s, nurse=%s, type=%s}", id, data, medico.fullName(), patient.fullName(), nurse.fullName(), visitType.toString());
    }

    public enum VisitType {
        ROUTINE_CHECKUP,
        SPECIALIST_CONSULTATION,
        URGENT_VISIT,
        PEDIATRIC_VISIT;
    }


}
