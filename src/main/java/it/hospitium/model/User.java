package it.hospitium.model;

import lombok.*;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * All the users that can log in the system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String email;
    @Column(nullable = false)
    private String pswHash;
    @Column(nullable = false,unique = true)
    private String CF;
    @Column(nullable = false)
    private String data_di_nascita;
    @Column(nullable = false)
    private String luogo_di_nascita;
    @Column(nullable = false)
    private Role role;

    public User(String firstName, String lastName, String email,String psw ,String CF,String data_di_nascita,String luogo_di_nascita, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        // assertEmail(email);
        this.email = email;
        if (psw == "") {
            this.pswHash = hashPsw(generatePsw());
        }else{
            assertPassword(psw);
            this.pswHash = hashPsw(psw);
        }
        this.CF = CF;
        this.data_di_nascita = data_di_nascita;
        this.luogo_di_nascita = luogo_di_nascita;
        this.role = role;
    }

    @SneakyThrows
    public static String hashPsw(String psw) {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] salt = new byte[16];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        salt = "kokarkokarkokar1".getBytes();
        md.update(salt);
        byte[] hashedPsw = md.digest(psw.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPsw) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    static void assertEmail(String email) {
        String regexPattern = "^[\\w.]+@[\\w.]+$";
        if (!Pattern.compile(regexPattern).matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static void assertPassword(String psw) {
        if (psw.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
    }


    public static String generatePsw () {
        String psw = "";
        for (int i = 0; i < 8; i++) {
            psw += (char) (Math.random() * 26 + 'a');
        }
        return psw;
    }

    // data_di_nascita should be in the format dd/mm/yyyy  e.g. 01/01/2000
    static void assertDate(String date) {
        String regexPattern = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$";
        if (!Pattern.compile(regexPattern).matcher(date).matches()) {
            throw new IllegalArgumentException("Invalid data_di_nascita format");
        }
    }

    public void setEmail(String email) {
        assertEmail(email);
        this.email = email;
    }

    public void setPassword(String psw) {
        assertPassword(psw);
        this.pswHash = hashPsw(psw);
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d,email=%s,role=%s}", id, email, role.toString());
    }

    public enum Role {
        MEDICO,NURSE,PATIENT, SECRETARY
    }
}
