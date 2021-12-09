package be.intec.repository;

import be.intec.models.Student;
import be.intec.services.exceptions.QueryException;
import be.intec.utils.JPAFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;


class StudentRepositoryTest {

    private final StudentRepository repository = new StudentRepository();

    private void createStudentWithJPA() {
        Student student = generateStudent();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        // CREATE
        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

    }


    // CODE COVERAGE


    private Student generateStudent() {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john@in.be");
        student.setAge(9);

        return student;
    }



    @Test
    void should_save_succeed() {

        Student student = generateStudent();
        student.setEmail("joey@mail.be");
        Long savedId = repository.save(student);

        Assertions.assertNotNull(savedId);
    }

    @Test
    void should_save_fail_when_student_is_duplicate() {

        Assertions.assertThrows(
                // THE CLASS NAME OF EXCEPTION WILL BE THROWN
                QueryException.class,
                () -> {
                    Student student = generateStudent();

                    // success ..
                    repository.save(student);
                    // throws an exception due duplicate entry .
                    repository.save(student);
                }
        );

    }

    @Test
    void should_save_fail_when_student_is_null() {

        Assertions.assertThrows(
                QueryException.class,
                () -> {
                    repository.save(null);
                }
        );

    }

    @Test
    void should_save_fail_when_student_name_has_less_than_2_chars() {

        Assertions.assertThrows(
                QueryException.class,
                () -> {
                    Student student = generateStudent();
                    student.setName("X");
                    student.setEmail("chef@mail.be");

                    repository.save(student);
                }
        );

    }

    @Test
    void should_exist_by_email_succeed_when_student_does_not_exist() {


        Boolean actual = repository.existsByEmail("chef@cook.be");

        Assertions.assertFalse(actual);
    }

    @Test
    void should_exist_by_email_succeed_when_student_exist() {

        createStudentWithJPA();
        Boolean actual = repository.existsByEmail("john@in.be");

        Assertions.assertTrue(actual);
    }


    @Test
    void should_update_name_succeed() {

        Student student = generateStudent();
        student.setName("Nikola");
        Long updatedId = repository.updateName(student.getId(), student.getName());

        Assertions.assertNotNull(updatedId);

    }

    @Test
    void should_update_name_fail_when_name_is_not_valid() {
        Assertions.assertThrows(
                QueryException.class,
                () -> {
                    Student student = generateStudent();
                    student.setName("X");

                    repository.updateName(student.getId(), student.getName());
                }
        );

    }

    @Test
    void should_update_email_succeed() {
        Student student = generateStudent();
        student.setEmail("Justin@be.intec");
        Long updatedId = repository.updateEmail(student.getId(), student.getEmail());

        Assertions.assertNotNull(updatedId);
    }

    @Test
    void should_update_email_fail_when_email_is_not_valid() {
        Assertions.assertThrows(
                QueryException.class,
                () -> {
                    repository.updateEmail(1l, null);
                }

        );


    }

    @Test
    void should_update_age_succeed() {
        Student student = generateStudent();
        student.setAge(40);
        Long updatedId = repository.updateAge(student.getId(), student.getAge());

        Assertions.assertNotNull(updatedId);
    }


}