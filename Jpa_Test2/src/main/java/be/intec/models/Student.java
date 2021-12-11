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
// @Table(name = "tbl_student") // OPTIONAL
public class Student { // Student -> student

    // JavaNamingConvention -> SnakeCaseNamingConvention
    @Id
    @GeneratedValue
    @Column(name = "student_id") // CUSTOMIZE COLUMN NAME
    Long id; // id -> id

    @Column(name = "naam")// OPTIONAL
    String name; // firstName -> first_name

    @Column(name = "leeftijd")
    Integer age;

    @Column(unique = true)
    String email;

}
