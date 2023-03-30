package ch.uzh.ifi.hase.soprafs23.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Lobby Representation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "LOBBY")
public class Lobby{
    @Id
    private Long pin;

    public void setPin(Long pin) {
        this.pin = pin;
    }

    public Long getPin() {
        return pin;
    }
}
