package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
// @Table(name = "tbl_exam")
public class Exam {

    @Id
    @GeneratedValue
    @Column(name = "examen_id")
    Long id;

    @Column(name = "naam")
    String name;

    @Column(name = "examen_datum")
    LocalDateTime examDate;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    @ManyToMany
    List<Question> questions;

}
