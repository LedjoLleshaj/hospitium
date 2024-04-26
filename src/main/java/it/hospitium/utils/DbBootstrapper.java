package it.hospitium.utils;

import it.hospitium.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Initializes an empty database with some data. This is because we're currently using an in-memory database and it
 * would be tedious to start fresh each time the app is rebuilt and restarted.
 */
@Component
public class DbBootstrapper {
    @Autowired
    UserRepository repoUser;

    @Autowired
    PatientRepository repoPatient;

    @Autowired
    MedicoRepository repoDoctor;

    @Autowired
    NurseRepository repoNurse;

    @Autowired
    AppointmentRepository repoAppointment;

    @Autowired
    VisitaRepository repoVisita;

    @Autowired
    SecretaryRepository repoSecretary;



    @PostConstruct
    public void bootstrap() {
        // Create a couple of users that are patients
        User user1 = new User("User", "Patient_One", "user.patient_one@hospitium.it", "user.patient_one@hospitium.it","CODICE_FISCALE1","29/08/2000","Bota me rrota", User.Role.PATIENT);
        User user2 = new User("User", "Patient_Two", "user.patient_two@hospitium.it", "user.patient_two@hospitium.it","CODICE_FISCALE2","00/00/0000","Bota me rrota", User.Role.PATIENT);
        User user3 = new User("User", "Patient_Three", "user.patient_three@hospitium.it", "user.patient_three@hospitium.it","CODICE_FISCALE3","00/00/0000","Bota me rrota", User.Role.PATIENT);
        User user4 = new User("User", "Patient_Four", "user.patient_four@hospitium.it", "user.patient_four@hospitium-it","CODICE_FISCALE4","00/00/0000","Bota me rrota", User.Role.PATIENT);
        // Create a couple of users that are doctors"Bota me rrota",
        User user5 = new User("User", "Doctor_One", "user.doctor_one@hospitium.it", "user.doctor_one@hospitium.it","CODICE_FISCALE5","00/00/0000","Bota me rrota", User.Role.MEDICO);
        User user6 = new User("User", "Doctor_Two", "user.doctor_two@hospitium.it", "user.doctor_two@hospitium.it","CODICE_FISCALE6","00/00/0000","Bota me rrota", User.Role.MEDICO);
        User user7 = new User("User", "Doctor_Three", "user.doctor_three@hospitium.it", "user.doctor_three@hospitium.it","CODICE_FISCALE7","00/00/0000","Bota me rrota", User.Role.MEDICO);
        // Create nurses
        User user8 = new User("User", "Nurse_One", "user.nurse_one@hospitium.it", "user.nurse_one@hospitium.it","CODICE_FISCALE8","00/00/0000","Bota me rrota", User.Role.NURSE);
        User user9 = new User("User", "Nurse_Two", "user.nurse_two@hospitium.it", "user.nurse_two@hospitium.it","CODICE_FISCALE9","00/00/0000","Bota me rrota", User.Role.NURSE);
        User user10 = new User("User", "Nurse_Three", "user.nurse_three@hospitium.it", "user.nurse_three@hospitium.it", "CODICE_FISCALE10","00/00/0000","Bota me rrota",User.Role.NURSE);
        // Create secretaries
        User user11 = new User("User", "Secretary_One", "user.secretary_one@hospitium.it", "user.secretary_one@hospitium.it","CODICE_FISCALE11","00/00/0000","Bota me rrota", User.Role.SECRETARY);
        User user12 = new User("User", "Secretary_Two", "user.secretary_two@hospitium.it", "user.secretary_two@hospitium.it","CODICE_FISCALE12","00/00/0000","Bota me rrota", User.Role.SECRETARY);




//        for (User user : List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10)) {
//            System.out.println(user);
//        }
        repoUser.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10,user11,user12));

        // Create a couple of doctors
        Medico doctor1 = new Medico(user5);
        Medico doctor2 = new Medico(user6);
        Medico doctor3 = new Medico(user7);
        repoDoctor.saveAll(List.of(doctor1, doctor2, doctor3));
//        for (Medico doctor : List.of(doctor1, doctor2, doctor3)) {
//            System.out.println(doctor);
//        }

        // Create a couple of patients
        Patient patient1 = new Patient("CODICE_SANITARIO", user1, doctor1);
        Patient patient2 = new Patient("CODICE_SANITARIO", user2, doctor1);
        Patient patient3 = new Patient("CODICE_SANITARIO", user3, doctor2);
        Patient patient4 = new Patient("CODICE_SANITARIO", user4, doctor2);
        repoPatient.saveAll(List.of(patient1, patient2, patient3, patient4));
//        for (Patient patient : List.of(patient1, patient2, patient3, patient4)) {
//            System.out.println(patient);
//        }


        // Create a couple of nurses
        Nurse nurse1 = new Nurse(user8,doctor1);
        Nurse nurse2 = new Nurse(user9,doctor2);
        Nurse nurse3 = new Nurse(user10,doctor1);
        repoNurse.saveAll(List.of(nurse1, nurse2, nurse3));
//        for (Nurse nurse : List.of(nurse1, nurse2, nurse3)) {
//            System.out.println(nurse);
//        }

        Secretary secretary1 = new Secretary(user11);
        Secretary secretary2 = new Secretary(user12);
        repoSecretary.saveAll(List.of(secretary1, secretary2));
        for (Secretary secretary : List.of(secretary1, secretary2)) {
            System.out.println(secretary);
        }


        // Create a couple of Appointments
        Appointment appointment1 = new Appointment("2021-06-01", Visita.VisitType.ROUTINE_CHECKUP, 1, doctor1, patient1);
        Appointment appointment2 = new Appointment("2021-06-02", Visita.VisitType.SPECIALIST_CONSULTATION, 2, doctor2, patient2);
        Appointment appointment3 = new Appointment("2021-06-03", Visita.VisitType.URGENT_VISIT, 3, doctor1, patient3);
        Appointment appointment4 = new Appointment("2021-06-04", Visita.VisitType.PEDIATRIC_VISIT, 4, doctor2, patient4);
        Appointment appointment5 = new Appointment("2021-06-04", Visita.VisitType.ROUTINE_CHECKUP, 4, doctor2, patient1);
        repoAppointment.saveAll(List.of(appointment1, appointment2, appointment3, appointment4, appointment5));
        for (Appointment appointment : List.of(appointment1, appointment2, appointment3, appointment4, appointment5)) {
            System.out.println(appointment);
        }

        // Create a couple of Visits
        Visita visit1 = new Visita("2021-06-01", "Result", Visita.VisitType.ROUTINE_CHECKUP, 5, doctor1, patient1, nurse1);
        Visita visit11 = new Visita("2021-06-11", "Result visit 2", Visita.VisitType.ROUTINE_CHECKUP, 5, doctor1, patient1, nurse1);
        Visita visit2 = new Visita("2021-06-02", "Result", Visita.VisitType.SPECIALIST_CONSULTATION, 6, doctor2, patient2, nurse2);
        Visita visit3 = new Visita("2021-06-03", "Result", Visita.VisitType.URGENT_VISIT, 5, doctor1, patient3, nurse3);
        Visita visit4 = new Visita("2021-06-04", "Result", Visita.VisitType.PEDIATRIC_VISIT, 6, doctor2, patient4, nurse2);
        repoVisita.saveAll(List.of(visit1, visit2, visit3, visit4, visit11));
//        for (Visita visit : List.of(visit1, visit2, visit3, visit4)) {
//            System.out.println(visit);
//        }
    }
}
