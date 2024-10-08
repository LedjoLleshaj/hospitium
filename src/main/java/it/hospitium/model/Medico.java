package it.hospitium.model;

import lombok.*;

import javax.persistence.*;
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
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Medico( User user) {

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
