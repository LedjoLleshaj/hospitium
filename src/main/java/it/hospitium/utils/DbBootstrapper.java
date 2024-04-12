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

    @PostConstruct
    public void bootstrap() {
        // Create a couple of patients
        User patient1 = new User("Patient", "One", "patient.one@hospitium.it", "patient.one@hospitium.it", User.Role.PATIENT);
        User patient2 = new User("Patient", "Two", "patient.two@hospitium.it", "patient.two@hospitium.it", User.Role.PATIENT);
        User patient3 = new User("Patient", "Three", "patient.three@hospitium.it", "patient.three@hospitium.it", User.Role.PATIENT);
        User patient4 = new User("Patient", "Four", "patient.four@hospitium.it", "patient.four@hospitium.it", User.Role.PATIENT);
        repoUser.saveAll(List.of(patient1, patient2, patient3, patient4));

        User medico = new User("B", "Medico", "a.medico@hospitium.it", "a.medico@hospitium.it", User.Role.MEDICO);
        repoUser.save(medico);

    }
}
