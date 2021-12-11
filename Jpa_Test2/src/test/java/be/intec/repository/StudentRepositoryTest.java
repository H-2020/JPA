package be.intec.repository;

import be.intec.models.Student;
import be.intec.services.exceptions.QueryException;
import be.intec.utils.JPAFactory;
import be.intec.utils.StudentUtils;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static be.intec.services.exceptions.ExceptionMessages.*;
import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private final StudentRepository repository = new StudentRepository();

    private static int emailIndex = 0;

    private String nextUniqueEmail() {
        return ("student" + (++emailIndex) + "@mail.be");
    }


    // @Order
    @Test
    void should_save_succeed() {


        Student student = new Student();
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());
        student.setName("John Doe");

        Long savedStudentId = repository.save(student);

        assertNotNull(savedStudentId);
        assertNotEquals(0L, savedStudentId);
    }

    @Test
    void should_save_fail_when_student_is_duplicate() {


        Student student = new Student();
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail("duplicate@email");
        student.setName("John Doe");

        StudentUtils.saveStudentToDatabase(student);

        Student duplicate = new Student();
        duplicate.setAge(new Random().nextInt(100) + 10);
        duplicate.setEmail("duplicate@email");
        duplicate.setName("John Doe");

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    // throws an exception due duplicate entry .
                    repository.save(duplicate);
                });

        // OPTIONAL
        String expectedMessage = STUDENT_ALREADY_EXISTS.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_save_fail_when_student_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.save(null);
                });

        String expectedMessage = STUDENT_INFO_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_save_fail_when_student_name_has_less_than_2_chars() {

        Student student = new Student();
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());

        student.setName("A");
        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    repository.save(student);
                });

        String expectedMessage = STUDENT_NAME_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_save_fail_when_student_email_is_null() {
        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(200) + 10);

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    repository.save(student);
                });

        String expectedMessage = STUDENT_EMAIL_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_save_fail_when_student_email_not_contains_character_atsign() {
        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(200) + 10);
        student.setEmail("justinintec.be");

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    repository.save(student);
                });

        String expectedMessage = STUDENT_EMAIL_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));


    }

    @Test
    void should_updateName_succeed() {

        Student student = new Student();
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());
        student.setName("John Doe");
//        student.setId(-500l);

        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Long updatedStudentId = repository.updateName(savedStudent.getId(), "Nikola");


        assertNotNull(updatedStudentId);
        assertTrue(updatedStudentId > 0);
    }

    @Test
    void should_update_name_fail_when_student_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateName(null, "Nikola");
                });

        String expectedMessage = STUDENT_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_name_fail_when_student_id_is_nagative_or_0() {


        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateName(-50l, "Nikola");
                });

        String expectedMessage = STUDENT_ID_IS_NOT_VALID
                .getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_name_fail_when_name_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateName(1l, null);
                });

        String expectedMessage = STUDENT_NAME_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_name_fail_when_name_is_shorter_than_2_characters() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateName(1l, "a");
                });

        String expectedMessage = STUDENT_NAME_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_name_fail_when_student_with_this_id_is_not_found() {

        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());

        StudentUtils.saveStudentToDatabase(student);

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    Optional<Long> oId = StudentUtils.getLastStudentId();

                    if (oId.isPresent()) {
                        repository.updateName(oId.get() + 1, "Nikola");
                    }

                });

        String expectedMessage = STUDENT_NOT_FOUND.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");

        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Long updatedStudentId = repository.updateEmail(savedStudent.getId(), "mynewmail@intec.be");

        //optional
        assertNotNull(updatedStudentId);
        //must
        assertTrue(updatedStudentId > 0);

    }

    @Test
    void should_update_email_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateEmail(null, "nikola@intec.be");
                });

        String expectedMessage = STUDENT_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_update_email_fail_when_id_is_smaller_than_0() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateEmail(-10l, "nikola@intec.be");
                });

        String expectedMessage = STUDENT_ID_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_fail_when_email_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateEmail(1l, null);
                });

        String expectedMessage = STUDENT_EMAIL_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_fail_when_email_does_not_contain_email_character() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateEmail(1l, "idonthave");
                });

        String expectedMessage = STUDENT_EMAIL_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_fail_when_student_with_this_id_is_not_found() {

        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());

        StudentUtils.saveStudentToDatabase(student);

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    Optional<Long> oId = StudentUtils.getLastStudentId();

                    if (oId.isPresent()) {
                        repository.updateEmail(oId.get() + 1, "dontcarenow@gmail.com");
                    }

                });

        String expectedMessage = STUDENT_NOT_FOUND.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_update_age_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");

        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Long updatedStudentId = repository.updateAge(savedStudent.getId(), 40);

        //optional
        assertNotNull(updatedStudentId);
        //must
        assertTrue(updatedStudentId > 0);

    }

    @Test
    void should_update_age_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateAge(null, 25);
                });

        String expectedMessage = STUDENT_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_update_age_fail_when_id_is_smaller_than_0() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateAge(-10l, 25);
                });

        String expectedMessage = STUDENT_ID_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_age_fail_when_age_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateAge(1l, null);
                });

        String expectedMessage = STUDENT_AGE_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_age_fail_when_age_is_smaller_than_7() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.updateAge(1l, 6);
                });

        String expectedMessage = STUDENT_AGE_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_age_fail_when_student_with_this_id_is_not_found() {

        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());

        StudentUtils.saveStudentToDatabase(student);

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    Optional<Long> oId = StudentUtils.getLastStudentId();

                    if (oId.isPresent()) {
                        repository.updateAge(oId.get() + 1, 15);
                    }

                });

        String expectedMessage = STUDENT_NOT_FOUND.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_delete_id_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");


        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Long deletedStudentId = repository.delete(savedStudent.getId());

        //optional
        assertNotNull(deletedStudentId);
        //must
        assertTrue(deletedStudentId > 0);

    }

    @Test
    void should_delete_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.delete(null);
                });

        String expectedMessage = STUDENT_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_delete_fail_when_id_is_smaller_than_0() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.delete(-10l);
                });

        String expectedMessage = STUDENT_ID_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_delete_email_fail_when_student_with_this_id_is_not_found() {

        Student student = new Student();
        student.setName("John Doe");
        student.setAge(new Random().nextInt(100) + 10);
        student.setEmail(nextUniqueEmail());

        StudentUtils.saveStudentToDatabase(student);

        Exception exception = assertThrows(
                QueryException.class,
                () -> {

                    Optional<Long> oId = StudentUtils.getLastStudentId();

                    if (oId.isPresent()) {
                        repository.delete(oId.get() + 1);
                    }

                });

        String expectedMessage = STUDENT_NOT_FOUND.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void should_exist_by_id_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");


        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Boolean existStudentId = repository.existsById(savedStudent.getId());

        //optional
        assertNotNull(existStudentId);
        //must
        assertTrue(existStudentId);

    }


    @Test
    void should_student_find_by_id_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");


        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Optional<Student> findStudentId = repository.findById(savedStudent.getId());


        //optional
        assertNotNull(findStudentId);
        //must
        assertTrue(findStudentId != null);

    }

    @Test
    void should_find_fail_when_id_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(null);
                });

        String expectedMessage = STUDENT_ID_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_find_fail_when_id_is_equal_to_zero_or_smaller_than_zero() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findById(-1l);
                });

        String expectedMessage = STUDENT_ID_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }



    @Test
    void should_exist_by_email_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");


        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Boolean existStudentId = repository.existsByEmail(savedStudent.getEmail());

        //optional
        assertNotNull(existStudentId);
        //must
        assertTrue(existStudentId);

    }
    @Test
    void should_find_student_by_email_succeed() {


        Student student = new Student();
        student.setAge(40);
        student.setEmail(nextUniqueEmail());
        student.setName("John ");


        Student savedStudent = StudentUtils.saveStudentToDatabase(student);

        Optional<Student> findStudentEmail = repository.findByEmail(savedStudent.getEmail());


        //optional
        assertNotNull(findStudentEmail);
        //must
        assertTrue(findStudentEmail != null);

    }


    @Test
    void should_find_failure_with_keyword_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findByEmail(null);
                });

        String expectedMessage = STUDENT_EMAIL_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_find_failure_student_by_email_contains_not_atcharacter() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.findByEmail("email");
                });

        String expectedMessage = STUDENT_EMAIL_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
    @Test
    void should_find_all_with_limit_succeed() {
        Student student = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.doe" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent = StudentUtils.saveStudentToDatabase(student);
        Student student2 = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.dash" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent2 = StudentUtils.saveStudentToDatabase(student2);
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(student);
        studentList.add(student2);
        List<Student> foundStudentList = repository.findAll(1,2);
        assertNotNull(foundStudentList);
        assertTrue(foundStudentList != null);
    }

    @Test
    void should_find_all_succeed() {
        Student student = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.doe" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent = StudentUtils.saveStudentToDatabase(student);
        Student student2 = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.dash" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent2 = StudentUtils.saveStudentToDatabase(student2);
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(student);
        studentList.add(student2);
        List<Student> foundStudentList = repository.findAll();
        assertNotNull(foundStudentList);
        assertTrue(foundStudentList != null);
    }


    @Test
    void should_search_with_keyword_all_succeed() {
        Student student = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.doe" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent = StudentUtils.saveStudentToDatabase(student);
        Student student2 = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.dash" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent2 = StudentUtils.saveStudentToDatabase(student2);
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(student);
        studentList.add(student2);
        List<Student> searchStudentList = repository.search("mail@intec");
        assertNotNull(searchStudentList);
        assertTrue(searchStudentList != null);
    }
    @Test
    void should_search_failure_student_by_keyword_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.search(null);
                });

        String expectedMessage = SEARCH_KEYWORD_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_search_failure_student_by_keyword_length_is_zero_or_smaller_than_zero() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.search("");
                });

        String expectedMessage = SEARCH_KEYWORD_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_search_with_min_age_and_max_age_succeed() {
        Student student = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.doe" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent = StudentUtils.saveStudentToDatabase(student);
        Student student2 = new Student();
        student.setAge(( new Random().nextInt(100) + 10));
        student.setEmail("john.dash" + (nextUniqueEmail()) + "@mail.be");
        Student savedStudent2 = StudentUtils.saveStudentToDatabase(student2);
        List<Student> studentList = new ArrayList<Student>();
        studentList.add(student);
        studentList.add(student2);
        List<Student> searchStudentList = repository.search(10,40);
        assertNotNull(searchStudentList);
        assertTrue(searchStudentList != null);
    }
    @Test
    void should_search_failure_min_age_and_max_age_are_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.search(null,null);
                });

        String expectedMessage = SEARCH_CRITERIA_IS_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_search_failure_minAge_is_smaller_than_7_or_maxAge_is_smaller_and_equal_to_minAge_or_maxAge_is_greater_than_200() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.search(6,250);
                });

        String expectedMessage = SEARCH_CRITERIA_IS_NOT_VALID.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


}