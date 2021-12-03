package be.intec.sandbox;

import be.intec.models.Course;
import be.intec.models.Exam;
import be.intec.models.Student;
import be.intec.repository.StudentRepository;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JPAApp {

    private static final StudentRepository studentRepository = new StudentRepository();

    public static void main(String[] args) {

        Student student1 = new Student();
        student1.setName("Justin Bieber");
        student1.setEmail("just@in.be");
        student1.setAge(9);

        Student student2 = new Student();
        student2.setName("Justin Timberlake");
        student2.setEmail("justin@tim.ber");
        student2.setAge(41);

        Course javaCourse = new Course();
        javaCourse.setName("Java 21 Juni");
        javaCourse.setStartDate(LocalDateTime.now().plusDays(4));

        Exam examJPA = new Exam();
        examJPA.setName("Java EE - JPA");
        examJPA.setExamDate(LocalDateTime.now());
        examJPA.setCourse(javaCourse);

        System.out.println("Student ID before persist: " + student1.getId());
        Long savedStudent1Id = studentRepository.save(student1);
        System.out.println("Student ID after persist: " + savedStudent1Id);

        System.out.println("Student ID before persist: " + student2.getId());
        Long savedStudent2Id = studentRepository.save(student2);
        System.out.println("Student ID after persist: " + savedStudent2Id);

        System.out.println("Student 2 before merge: " + student2);
        Long updatedStudent2Id = studentRepository.updateEmail(savedStudent2Id, "john@doe.com");


    }
}
