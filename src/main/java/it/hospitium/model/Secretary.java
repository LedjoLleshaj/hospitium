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
public class Secretary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Secretary(User user) {

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
