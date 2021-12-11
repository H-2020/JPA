package be.intec.repository;

import be.intec.models.Student;
import be.intec.services.exceptions.QueryException;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static be.intec.services.exceptions.ExceptionMessages.*;

// VRAAG 01: Ga ik voor deze methode een test-code schrijven? JA
// VRAAG 02: Hoeveel scenarios heb ik hier? SUCCESS | FAILURE | GEEN
// VRAAG 03: Hoeveel sub-scenarios heb ik voor succes? > 0
// maak een test-methode voor elk scenario aan.
// VRAAG 04: Hoeveel sub-scenarios heb ik voor failure? > 0
// maak een test-methode voor elk scenario aan.
// VRAAG 05: Hoeveel in totaal moet ik test-methoden aanmaken?

public class StudentRepository {

    public Long save(Student student) throws QueryException {

        if (student == null) {
            throw new QueryException(STUDENT_INFO_REQUIRED.getBody());
        }

        // ATTENTION !! EACH CONDITION IS AN APART FAILURE SCENARIO HERE
        if (student.getEmail() == null) {
            throw new QueryException(STUDENT_EMAIL_IS_REQUIRED.getBody());
        }

        if (!student.getEmail().contains("@")) {
            throw new QueryException(STUDENT_EMAIL_IS_NOT_VALID.getBody());
        }

        if (student.getName().length() < 2) {
            throw new QueryException(STUDENT_NAME_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // .getSingleResult() throws NoResultException when there is no result found.
        // we need to handle the exception to be able to continue for the remaining instructions.
        Student foundStudent = null;
        try {
            foundStudent = (Student) manager
                    .createNativeQuery("SELECT * FROM student WHERE email = :qpEmail", Student.class)
                    .setParameter("qpEmail", student.getEmail())
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        if (foundStudent != null) {
            throw new QueryException(STUDENT_ALREADY_EXISTS.getBody());
        }

        manager.getTransaction().begin();

        // CREATE
        manager.persist(student);

        manager.getTransaction().commit();

        if (student.getId() == null) {
            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
        }

        Long lastSavedId = student.getId();

        manager.close();

        return lastSavedId;
    }

    public Long updateName(Long id, String newName) throws QueryException {

        if (id == null) {
            throw new QueryException(STUDENT_ID_IS_REQUIRED.getBody());
        }

        if (id <= 0L) {
            throw new QueryException(STUDENT_ID_IS_NOT_VALID.getBody());
        }

        if (newName == null) {
            throw new QueryException(STUDENT_NAME_IS_REQUIRED.getBody());
        }

        if (newName.length() < 2) {
            throw new QueryException(STUDENT_NAME_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent == null) {
            throw new QueryException(STUDENT_NOT_FOUND.getBody());
        }

        foundStudent.setName(newName);

        // UPDATE ENTITY
        manager.merge(foundStudent);

        manager.getTransaction().commit();

        if (foundStudent.getId() == null) {
            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
        }

        Long lastUpdatedId = foundStudent.getId();

        manager.close();

        return lastUpdatedId;

    }

    public Long updateEmail(Long id, String newEmail) throws QueryException {

        if (id == null) {
            throw new QueryException(STUDENT_ID_IS_REQUIRED.getBody());
        }

        if (id <= 0L) {
            throw new QueryException(STUDENT_ID_IS_NOT_VALID.getBody());
        }

        if (newEmail == null) {
            throw new QueryException(STUDENT_EMAIL_IS_REQUIRED.getBody());
        }

        if (!newEmail.contains("@")) {
            throw new QueryException(STUDENT_EMAIL_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent == null) {
            throw new QueryException(STUDENT_NOT_FOUND.getBody());
        }

        // .getSingleResult() throws NoResultException when there is no result found.
        // we need to handle the exception to be able to continue for the remaining instructions.
        Student foundStudentByEmail = null;
        try {
            foundStudentByEmail = (Student) manager
                    .createNativeQuery("SELECT * FROM student WHERE email = :qpEmail", Student.class)
                    .setParameter("qpEmail", newEmail)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        if (foundStudentByEmail != null) {
            throw new QueryException(STUDENT_ALREADY_EXISTS.getBody());
        }

        foundStudent.setEmail(newEmail);

        // UPDATE ENTITY
        manager.merge(foundStudent);

        manager.getTransaction().commit();

        if (foundStudent.getId() == null) {
            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
        }

        Long lastUpdatedId = foundStudent.getId();

        manager.close();

        return lastUpdatedId;
    }

    public Long updateAge(Long id, Integer newAge) throws QueryException {

        if (id == null) {
            throw new QueryException(STUDENT_ID_IS_REQUIRED.getBody());
        }

        if (id <= 0L) {
            throw new QueryException(STUDENT_ID_IS_NOT_VALID.getBody());
        }

        if (newAge == null) {
            throw new QueryException(STUDENT_AGE_IS_REQUIRED.getBody());
        }

        if (newAge < 7) {
            throw new QueryException(STUDENT_AGE_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent == null) {
            throw new QueryException(STUDENT_NOT_FOUND.getBody());
        }

        foundStudent.setAge(newAge);

        // UPDATE ENTITY
        manager.merge(foundStudent);

        manager.getTransaction().commit();

        if (foundStudent.getId() == null) {
            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
        }

        Long lastUpdatedId = foundStudent.getId();

        manager.close();

        return lastUpdatedId;
    }

    public Long delete(Long id) throws QueryException {

        if (id == null) {
            throw new QueryException(STUDENT_ID_IS_REQUIRED.getBody());
        }

        if (id <= 0L) {
            throw new QueryException(STUDENT_ID_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if (foundStudent == null) {
            throw new QueryException(STUDENT_NOT_FOUND.getBody());
        }

        Long lastDeletedId = foundStudent.getId();

        // REMOVE THE ENTITY
        manager.remove(foundStudent);

        manager.getTransaction().commit();

//        if (foundStudent.getId() != null) {
//            throw new QueryException(UNDEFINED_EXCEPTION.getBody());
//        }

        manager.close();

        return lastDeletedId;
    }

    public Boolean existsById(Long id) {
        return (findById(id).isPresent());
    }

    public Optional<Student> findById(Long id) throws QueryException {

        if (id == null) {
            throw new QueryException(STUDENT_ID_IS_REQUIRED.getBody());
        }

        if (id <= 0L) {
            throw new QueryException(STUDENT_ID_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        manager.close();

        return Optional.ofNullable(foundStudent);
    }

    public Boolean existsByEmail(String email) {
        return (findByEmail(email).isPresent());
    }

    public Optional<Student> findByEmail(String email) throws QueryException {

        if (email == null) {
            throw new QueryException(STUDENT_EMAIL_IS_REQUIRED.getBody());
        }

        if (!email.contains("@")) {
            throw new QueryException(STUDENT_EMAIL_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // .getSingleResult() throws NoResultException when there is no result found.
        // we need to handle the exception to be able to continue for the remaining instructions.
        Student foundStudent = null;
        try {
            foundStudent = (Student) manager
                    .createNativeQuery("SELECT * FROM student WHERE email = :qpEmail", Student.class)
                    .setParameter("qpEmail", email)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        manager.close();

        return Optional.ofNullable(foundStudent);
    }

    public List<Student> findAll(Integer limit, Integer offset) throws QueryException {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Student> studentList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM student LIMIT :qpLimit OFFSET :qpOffset", Student.class)
                        .setParameter("qpLimit", limit)
                        .setParameter("qpOffset", offset)
                        .getResultList());

        manager.close();

        return studentList;
    }

    public List<Student> findAll() throws QueryException {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Student> studentList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM student", Student.class)
                        .getResultList());

        manager.close();

        return studentList;
    }

    public List<Student> search(String keyword) throws QueryException {

        if (keyword == null) {
            throw new QueryException(SEARCH_KEYWORD_IS_REQUIRED.getBody());
        }

        if (keyword.length() <= 0L) {
            throw new QueryException(SEARCH_KEYWORD_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Student> studentList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM student" +
                                " WHERE email LIKE :qpEmail OR naam LIKE :qpName", Student.class)
                        .setParameter("qpEmail", keyword)
                        .setParameter("qpName", keyword)
                        .getResultList());

        manager.close();

        return studentList;

    }

    public List<Student> search(Integer minAge, Integer maxAge) throws QueryException {

        if (minAge == null && maxAge == null) {
            throw new QueryException(SEARCH_CRITERIA_IS_REQUIRED.getBody());
        }

        if (minAge < 7L || maxAge <= minAge || maxAge > 200) {
            throw new QueryException(SEARCH_CRITERIA_IS_NOT_VALID.getBody());
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<Student> studentList = Collections.unmodifiableList(manager
                .createNativeQuery("SELECT * FROM student WHERE leeftijd > :qpMinAge AND leeftijd < :qpMaxAge")
                .setParameter("qpMinAge", minAge)
                .setParameter("qpMaxAge", maxAge)
                .getResultList()
        );

        manager.close();

        return studentList;

    }

}
