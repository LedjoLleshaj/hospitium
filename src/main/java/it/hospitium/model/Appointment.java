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
    private String time;
    @Column(nullable = false)
    private String note;
    @Column(nullable = false)
    private VisitType visitType;
    @Column(nullable = false)
    private Integer urgenza;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    public Appointment(String data, String time, String note, VisitType visitType, Integer urgenza, Medico medico,Nurse nurse,
            Patient patient, Child child) {
        this.data = data;
        this.time = time;
        this.note = note;
        this.visitType = visitType;
        this.urgenza = urgenza;
        this.medico = medico;
        this.nurse = nurse;
        this.patient = patient;
        this.child = child;
    }

    @Override
    public String toString() {
        return String.format("Appointment{id=%d, data_di_nascita=%s, medico=%s, patient=%s, type=%s}", id, data,
                medico.fullName(), patient.fullName(), visitType.toString());
    }

    public static String formattedType(VisitType type) {
        switch (type) {
            case ROUTINE_CHECKUP:
                return "Routine Checkup";
            case SPECIALIST_CONSULTATION:
                return "Specialist Consultation";
            case URGENT_VISIT:
                return "Urgent Visit";
            case PEDIATRIC_VISIT:
                return "Pediatric Visit";
            case PRELIEVI:
                return "Prelievi";
            case MEDICATION:
                return "Medication";
            default:
                return "Unknown";
        }
    }

}
