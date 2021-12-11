package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
// @Table(name = "tbl_course")
public class Course {

    @Column(name = "course_id")
    @Id
    Integer id;

    @Column(name = "naam")
    String name;

    @Column(name = "start_datum")
    LocalDateTime startDate;

    // OPTIONAL
    @OneToMany(mappedBy = "course")
    List<Exam> exams;

}
