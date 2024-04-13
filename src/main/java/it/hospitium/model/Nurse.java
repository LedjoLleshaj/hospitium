package it.hospitium.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Model for doctor.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;


    public Nurse(User user, Medico medico) {

        this.user = user;
        this.medico = medico;

    }

    public String fullName() {
        return user.fullName();
    }

    @Override
    public String toString() {
        return String.format("User{id=%d,email=%s}", id, user.getEmail());
    }


}
