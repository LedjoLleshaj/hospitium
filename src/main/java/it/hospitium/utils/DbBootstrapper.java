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

    @Autowired
    ChildRepository repoChild;



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


        // Create a couple of children
        User child1 = new User("Child_user", "Child_One", "","","CODICE_FISCALE_child1","00/00/0000","Bota me rrota", User.Role.PATIENT);
        User child2 = new User("Child_user", "Child_Two", "","","CODICE_FISCALE_child2","00/00/0000","Bota me rrota", User.Role.PATIENT);
        User child3 = new User("Child_user", "Child_Three", "","","CODICE_FISCALE_child3","00/00/0000","Bota me rrota", User.Role.PATIENT);
        User child4 = new User("Child_user", "Child_Four", "","","CODICE_FISCALE_child4","00/00/0000","Bota me rrota", User.Role.PATIENT);
        repoUser.saveAll(List.of(child1, child2, child3, child4));

        Child child11 = new Child("CODICE_SANITARIO", child1, doctor1, patient1);
        Child child12 = new Child("CODICE_SANITARIO", child2, doctor1, patient1);
        Child child13 = new Child("CODICE_SANITARIO", child3, doctor2, patient2);
        Child child14 = new Child("CODICE_SANITARIO", child4, doctor2, patient2);
        repoChild.saveAll(List.of(child11, child12, child13, child14));
//        for (Child child : List.of(child11, child12, child13, child14)) {
//            System.out.println(child);
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
//        for (Secretary secretary : List.of(secretary1, secretary2)) {
//            System.out.println(secretary);
//        }


        // Create a couple of Appointments
        Appointment appointment1 = new Appointment("2021-06-01T9:00", "9:00","Note 1", Visita.VisitType.ROUTINE_CHECKUP, 1, doctor1, null,patient1,null);
        Appointment appointment2 = new Appointment("2021-06-02T9:30","09:30", "Note 2", Visita.VisitType.SPECIALIST_CONSULTATION, 2, doctor2, null,patient2,null);
        Appointment appointment3 = new Appointment("2021-06-03T10:00", "10:00", "Note 3", Visita.VisitType.URGENT_VISIT, 3, doctor1, null,patient3,null);
        Appointment appointment4 = new Appointment("2021-06-04T15:00", "15:00", "Note 4", Visita.VisitType.PEDIATRIC_VISIT, 4, doctor2, null,patient4,null);
        Appointment appointment5 = new Appointment("2021-06-04T15:30","15:30", "Note 5", Visita.VisitType.ROUTINE_CHECKUP, 4, doctor2, null,patient1,null);
        repoAppointment.saveAll(List.of(appointment1, appointment2, appointment3, appointment4, appointment5));
//        for (Appointment appointment : List.of(appointment1, appointment2, appointment3, appointment4, appointment5)) {
//            System.out.println(appointment);
//        }

        // Create a couple of Appointments for children
        Appointment appointment11 = new Appointment("2021-06-01T9:00", "9:00","Note 1", Visita.VisitType.ROUTINE_CHECKUP, 1, doctor1, null,patient1,child11);
        Appointment appointment12 = new Appointment("2021-06-02T9:30","09:30", "Note 2", Visita.VisitType.SPECIALIST_CONSULTATION, 2, doctor2, null,patient1,child12);
        Appointment appointment13 = new Appointment("2021-06-03T10:00", "10:00", "Note 3", Visita.VisitType.URGENT_VISIT, 3, doctor1, null,patient2,child13);
        Appointment appointment14 = new Appointment("2021-06-04T15:00", "15:00", "Note 4", Visita.VisitType.PEDIATRIC_VISIT, 4, doctor2, null,patient2,child14);
        repoAppointment.saveAll(List.of(appointment11, appointment12, appointment13, appointment14));
//        for (Appointment appointment : List.of(appointment11, appointment12, appointment13, appointment14)) {
//            System.out.println(appointment);
//        }

        // Create a couple of appointments for nurses
        Appointment appointment21 = new Appointment("2021-06-01T9:00", "9:00","Note 1", Visita.VisitType.PRELIEVI, 1,doctor1, nurse1,patient1,null);
        Appointment appointment22 = new Appointment("2021-06-02T9:30","09:30", "Note 2", Visita.VisitType.MEDICATION, 2,doctor1, nurse1,patient2,null);

        repoAppointment.saveAll(List.of(appointment21, appointment22));
        for (Appointment appointment : List.of(appointment21, appointment22)) {
            System.out.println(appointment);
        }


        // Create a couple of Visits
        Visita visit1 = new Visita("2021-06-01T9:00", "Result", Visita.VisitType.ROUTINE_CHECKUP, 5, doctor1, patient1,null, null);
        Visita visit2 = new Visita("2021-06-02T9:00", "Result", Visita.VisitType.SPECIALIST_CONSULTATION, 6, doctor2, patient2,null, null);
        Visita visit3 = new Visita("2021-06-03T9:00", "Result", Visita.VisitType.URGENT_VISIT, 5, doctor1, patient3,null, null);
        Visita visit4 = new Visita("2021-06-04T9:00", "Result", Visita.VisitType.PEDIATRIC_VISIT, 6, null, patient1,null, nurse1);
        repoVisita.saveAll(List.of(visit1, visit2, visit3, visit4));
        for (Visita visit : List.of(visit1, visit2, visit3, visit4)) {
            System.out.println(visit);
        }

        // nurse visit
        Visita visit5 = new Visita("2021-06-01T9:00", "Result", Visita.VisitType.PRELIEVI, 5, null, patient1,null, nurse1);
        Visita visit6 = new Visita("2021-06-02T9:00", "Result", Visita.VisitType.MEDICATION, 6, null, patient2,null, nurse1);

        repoVisita.saveAll(List.of(visit5, visit6));

        // Create a couple of Visits for children
        Visita visit11 = new Visita("2021-06-01T9:00", "Result1", Visita.VisitType.ROUTINE_CHECKUP, 5, doctor1, patient1,child11, null);
        Visita visit12 = new Visita("2021-06-02T9:00", "Result2", Visita.VisitType.SPECIALIST_CONSULTATION, 6, doctor2, patient1,child12, null);
        Visita visit13 = new Visita("2021-06-03T9:00", "Result3", Visita.VisitType.URGENT_VISIT, 5, doctor1, patient2,child13, null);
        Visita visit14 = new Visita("2021-06-04T9:00", "Result4", Visita.VisitType.PRELIEVI, 6, null, patient2,child14, nurse2);
        repoVisita.saveAll(List.of(visit11, visit12, visit13, visit14));
        for (Visita visit : List.of(visit11, visit12, visit13, visit14)) {
            System.out.println(visit);
        }
    }
}
