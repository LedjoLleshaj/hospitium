package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * All the patients that have a user login or their parent has a login in the user table
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String CS;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Patient(String CS,User user) {
        this.CS = CS;
        this.user = user;
    }

    public String fullName() {
        return user.fullName();
    }

    @Override
    public String toString() {
        return String.format("User{id=%d,email=%s}", id, user.getEmail());
    }

}
