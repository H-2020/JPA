package be.intec.sandbox;

import be.intec.models.Course;
import be.intec.models.Exam;
import be.intec.models.Student;
import be.intec.repository.CourseRepository;
import be.intec.repository.ExamRepository;
import be.intec.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;


public class JPAApp {

    private static final StudentRepository studentRepository = new StudentRepository();
    private static final CourseRepository courseRepository = new CourseRepository();
    private static final ExamRepository examRepository = new ExamRepository();

    public static void main(String[] args) {

        Student student1 = new Student();
        student1.setName("Justin Bieber");
        student1.setEmail("just@in.be");
        student1.setAge(9);

        Student student2 = new Student();
        student2.setName("Justin Timberlake");
        student2.setEmail("justin@tim.ber");
        student2.setAge(41);

      //  studentRepository.save(student1);
      //  studentRepository.save(student2);

        // studentRepository.updateEmail(9L, "justin@timberlake.com");

       // studentRepository.updateEmail(-1L, "nonexistent@student.com");

        // studentRepository.delete(8L);

        // studentRepository.delete(55L);

//       Optional<Student> oStudent =  studentRepository.findById(25L);
//
//       // IF oStudent has non-null Student instance
//       if(oStudent.isPresent()) {
//
//           Student foundStudent = oStudent.get();
//           System.out.println(foundStudent);
//
//       } else {
//           System.err.println("Student with " + 9L + " DOES NOT exists.. ");
//       }

//       List<Student> students = studentRepository.findAll();
//
//        for (Student student : students) {
//            System.out.println(student);
//        }
//
//
//        System.out.println("My code is complete..");

        Course javaCourse = new Course();
        javaCourse.setName("Java 21 Juni");
        javaCourse.setStartDate(LocalDateTime.now().plusDays(4));

        Exam examJPA = new Exam();
        examJPA.setName("Java EE - JPA");
        examJPA.setExamDate(LocalDateTime.now());
        examJPA.setCourse(javaCourse);

//        courseRepository.save(javaCourse);
//        examRepository.save(examJPA);
//

        //courseRepository.updateName(5,"python");
        List<Student> students =studentRepository.search("just@in.be");
        for (Student student : students) {
            System.out.println(student);
        }
    }
}
