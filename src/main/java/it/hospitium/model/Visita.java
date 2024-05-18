package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private VisitType type;
    @Column(nullable = false)
    private Integer insertedBy;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;
    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;

    public Visita(String data, String Result, VisitType type, Integer insertedBy, Medico medico, Patient patient,Child child, Nurse nurse) {
        this.data = data;
        this.Result = Result;
        this.type = type;
        this.insertedBy = insertedBy;
        this.medico = medico;
        this.patient = patient;
        this.child = child;
        this.nurse = nurse;
    }

    @Override
    public String toString() {
        return String.format("Visit{id=%d, data_di_nascita=%s, medico=%s, patient=%s, nurse=%s, type=%s}", id, data, medico.fullName(), patient.fullName(), nurse.fullName(), type.toString());
    }

    public enum VisitType {
        ROUTINE_CHECKUP,
        SPECIALIST_CONSULTATION,
        URGENT_VISIT,
        PEDIATRIC_VISIT;
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
             default:
                 return "Unknown";
         }
    }

    public static VisitType fromString(String type) {
        switch (type) {
            case "Routine Checkup":
                return VisitType.ROUTINE_CHECKUP;
            case "Specialist Consultation":
                return VisitType.SPECIALIST_CONSULTATION;
            case "Urgent Visit":
                return VisitType.URGENT_VISIT;
            case "Pediatric Visit":
                return VisitType.PEDIATRIC_VISIT;
            default:
                return null;
        }
    }

    public static List<String> getVisitCategories() {
        List<String> enumStrings = new ArrayList<>();

        // Iterate over enum constants and add their names to the list
        for (VisitType value : VisitType.values()) {
            enumStrings.add(formattedType(value));
        }
        for (String s : enumStrings) {
            System.out.println(s);
        }
        return enumStrings;

    }

}
