package be.intec.sandbox;

import be.intec.models.Course;
import be.intec.models.Exam;
import be.intec.models.Student;

import java.time.LocalDateTime;

public class TestApp {

    public static void main(String[] args) {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Justin Bieber");
        student1.setAge(9);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Justin Timberlake");
        student2.setAge(41);

        Course javaCourse = new Course();
        javaCourse.setId(2);
        javaCourse.setName("Java 21 Juni");
        javaCourse.setStartDate(LocalDateTime.now().plusDays(4));

        Exam examJPA = new Exam();
        examJPA.setId(1L);
        examJPA.setName("Java EE - JPA");
        examJPA.setExamDate(LocalDateTime.now());
        examJPA.setCourse(javaCourse);

        
    }
}
