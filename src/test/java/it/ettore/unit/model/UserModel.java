package it.ettore.unit.model;

import it.hospitium.model.User;
import org.junit.Before;
import org.junit.Test;

import static it.hospitium.model.User.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserModel {
    private User medico;
    private User patient;
    private User nurse;
    private User secretary;
    @Before
    public void setUp() {
        // Initialize medico
        medico = new User("Medico", "Medici", "medico@example.com", "password", "MED123", "01/01/1990", User.Role.MEDICO);

        // Initialize patient
        patient = new User("Patient", "Paziente", "patient@example.com", "password", "PAT456", "01/01/1980", User.Role.PATIENT);

        // Initialize nurse
        nurse = new User("Nurse", "Infermiere", "nurse@example.com", "password", "NUR789", "01/01/1985", User.Role.NURSE);

        // Initialize secretary
        secretary = new User("Secretary", "Segretario", "secretary@example.com", "password", "SEC012", "01/01/1995", User.Role.SECRETARY);
    }
    @Test
    public void testMedico() {
        // Test medico properties
        assertNotNull(medico);

        // Verify that the fields are correctly set
        assertEquals("Medico", medico.getFirstName());
        assertEquals("Medici", medico.getLastName());
        assertEquals("medico@example.com", medico.getEmail());
        assertEquals("MED123", medico.getCF());
        assertEquals("01/01/1990", medico.getDate());
    }

    @Test
    public void testPatient() {
        // Test medico properties
        assertNotNull(patient);

        // Verify that the fields are correctly set
        assertEquals("Patient", patient.getFirstName());
        assertEquals("Paziente", patient.getLastName());
        assertEquals("patient@example.com", patient.getEmail());
        assertEquals("PAT456", patient.getCF());
        assertEquals("01/01/1980", patient.getDate());
    }

    @Test
    public void testNurse() {
        // Test medico properties
        assertNotNull(nurse);

        // Verify that the fields are correctly set
        assertEquals("Nurse", nurse.getFirstName());
        assertEquals("Infermiere", nurse.getLastName());
        assertEquals("nurse@example.com", nurse.getEmail());
        assertEquals("NUR789", nurse.getCF());
        assertEquals("01/01/1985", nurse.getDate());
    }

    @Test
    public void testSecretary() {
        // Test medico properties
        assertNotNull(secretary);

        // Verify that the fields are correctly set
        assertEquals("Secretary", secretary.getFirstName());
        assertEquals("Segretario", secretary.getLastName());
        assertEquals("secretary@example.com", secretary.getEmail());
        assertEquals("SEC012", secretary.getCF());
        assertEquals("01/01/1995", secretary.getDate());
    }

    @Test
        public void testHashPsw() {
        User user = medico;
        User user1 = patient;
        User user2 = nurse;
        User user3 = secretary;
            // The same password should get hashed to the same digest
            assertEquals(hashPsw("password"), medico.getPswHash());
            assertEquals(hashPsw("password"), patient.getPswHash());
            assertEquals(hashPsw("password"), nurse.getPswHash());
            assertEquals(hashPsw("password"), secretary.getPswHash());
            // Test with a known input and expected output
        }

    @Test
    public void getEmail() {
        assertEquals("medico@example.com", medico.getEmail());
        assertEquals("patient@example.com", patient.getEmail());
        assertEquals("nurse@example.com", nurse.getEmail());
        assertEquals("secretary@example.com", secretary.getEmail());
    }

    @Test
    public void testAssertPassword_Valid() {
        // Valid password with length >= 8
        String validPassword = "password";
        assertPassword(validPassword); // Should not throw an exception
    }

    @Test
    public void testAssertPassword_TooShort() {
        // Invalid password with length < 8
        String invalidPassword = "1234567";
        assertThrows(IllegalArgumentException.class, () -> assertPassword(invalidPassword));
    }

    @Test
    public void dateValid(){
        String valDate = "01/01/1990";
                assertDate(valDate);
    }

    @Test
    public void testAssertDate_Invalid() {
        // Invalid date format
        String invalidDate = "2022/01/01";
        assertThrows(IllegalArgumentException.class, () -> assertDate(invalidDate));
    }

    @Test
    public void setEmail() {
        User user = medico;
        user.setEmail("medico@example.com");
        assertEquals("medico@example.com", user.getEmail());
    }

    @Test
    public void testSetPassword_Valid() {
        User user = medico;
        // Valid password
        String validPassword = "password";
        user.setPassword(validPassword);
        assertEquals("Hashed password should match", hashPsw(validPassword), user.getPswHash());
    }

    @Test
    public void testSetPassword_Invalid() {
        User user = medico;
        // Invalid password (too short)
        String invalidPassword = "short";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> user.setPassword(invalidPassword));
    }


}

