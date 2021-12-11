package be.intec.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Question {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "titel")
    String header;

    @Column(name = "content")
    String content;

    @Column(name = "waarde")
    Float value;

    @ManyToMany
    List<Exam> exams;

}
