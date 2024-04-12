package it.hospitium.model;

import lombok.*;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Model for doctor.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pswHash;

//    // For the professor
//    @OneToMany(mappedBy = "professor")
//    private List<Course> coursesTaught = new ArrayList<>();
//
//    // For the student
//    @ManyToMany(mappedBy = "studentsRequesting")
//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
//    private List<Course> coursesRequesting = new ArrayList<>();
//    @ManyToMany(mappedBy = "studentsJoined")
//    @Getter(AccessLevel.NONE)
//    @Setter(AccessLevel.NONE)
//    private List<Course> coursesJoined = new ArrayList<>();

    public Medico(String firstName, String lastName, String email, String psw) {
        this.firstName = firstName;
        this.lastName = lastName;
        assertEmail(email);
        this.email = email;
        assertPassword(psw);
        this.pswHash = hashPsw(psw);
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

    static void assertPassword(String psw) {
        if (psw.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
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
        return String.format("User{id=%d,email=%s}", id, email);
    }


}
