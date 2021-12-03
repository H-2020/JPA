package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

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

}
