package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Teacher {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "voornaam")
    String firstName;

    @Column(name = "familienaam")
    String lastName;

    @Column(unique = true)
    String email;

    @Column(name = "wachtwoord")
    String password;

    @Column(name = "afdeling")
    String department;

}
