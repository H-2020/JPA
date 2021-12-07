package be.intec.repository;

import be.intec.models.Student;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentRepository {

    private static final String STUDENT_NOT_FOUND = "Student NOT found!";

    public Long save(Student student) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();
        // CREATE
        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        return student.getId();
    }

    public Long updateName(Long id, String newName) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        // FLAG VARIABLE
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent != null) {

            foundStudent.setName(newName);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println(STUDENT_NOT_FOUND);
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long updateEmail(Long id, String newEmail) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent != null) {

            foundStudent.setEmail(newEmail);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println(STUDENT_NOT_FOUND);
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long updateAge(Long id, Integer newAge) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent != null) {

            foundStudent.setAge(newAge);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println(STUDENT_NOT_FOUND);
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long delete(Long id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // BEGIN TRANSACTION
        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        // IF FOUND
        if (foundStudent != null) {
            // REMOVE THE ENTITY
            manager.remove(foundStudent);

            // SEND EXECUTION REQUEST TO DATABASE SERVER
            manager.getTransaction().commit();

            manager.close();

            return id;

        } else {

            System.err.println(STUDENT_NOT_FOUND);
            return -1L;
        }
    }

    // RETURN NEVER NEVER NEVER NULL, AND NEVER SAY NEVER :)
    public Optional<Student> findById(Long id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        manager.getTransaction().commit();

        Optional<Student> oStudent = Optional.ofNullable(foundStudent);

        manager.close();

        return oStudent;
    }

    public List<Student> findAll() {

        List<Student> studentList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery("SELECT * FROM student", Student.class);
        studentList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return studentList;
    }

    public List<Student> search(String keyword) {
        //String searchQuery = "SELECT * FROM student WHERE email LIKe " + keyword + " OR name LIKE " + keyword;

        List<Student> studentList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery(("SELECT * FROM student s " +
                        "WHERE s.naam LIKE '%" + keyword + "%'" +
                        "OR s.email LIKE '%" + keyword + "%'"), Student.class);
        studentList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return studentList;

    }

}
