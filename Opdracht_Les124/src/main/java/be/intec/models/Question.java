package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue
    Long id;

    String header;
    String content;
    Float value;

    @ManyToMany
    List<Exam> exams;


}
