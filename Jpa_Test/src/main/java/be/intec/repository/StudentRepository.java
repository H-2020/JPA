package be.intec.repository;

import be.intec.models.Student;
import be.intec.services.exceptions.QueryException;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// VRAAG 01: Ga ik voor deze methode een test-code schrijven? JA
// VRAAG 02: Hoeveel scenarios heb ik hier? SUCCESS | FAILURE | GEEN
// VRAAG 03: Hoeveel sub-scenarios heb ik voor succes? > 0
// maak een test-methode voor elk scenario aan.
// VRAAG 04: Hoeveel sub-scenarios heb ik voor failure? > 0
// maak een test-methode voor elk scenario aan.
// VRAAG 05: Hoeveel in totaal moet ik test-methoden aanmaken?

public class StudentRepository {

    private static final String STUDENT_NOT_FOUND = "Student NOT found!";

    //V01->JA
    public Boolean existsByEmail(String email) {
        // V02 -> SUCCESS
        // V03 -> 2
        // V04 -> 0

        // SUCCESS + FAILURE = 1 + 0= 1
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query searchQuery = manager
                .createNativeQuery("SELECT COUNT(*) FROM student WHERE email LIKE :qpEmail")
                .setParameter("qpEmail", email);


         manager.getTransaction().commit();
        //2 possiblities :count positive| count is zero
        int count = searchQuery.getSingleResult() != null ? Integer.parseInt(searchQuery.getSingleResult().toString()) : 0;
        manager.close();

        //count positive | zero
        //V03++->

        return (count > 0);


    }

    // V01 -> JA
    public Long save(Student student) throws QueryException {

        // V02 -> 2
        // V01 -> 1
        // V04 -> 4

        // SUCCESS + FAILURE = 1 + 4 = 5

        if (student == null) {
            throw new QueryException("Student information is required.");
        }

        if (existsByEmail(student.getEmail()).equals(Boolean.TRUE)) {
            throw new QueryException("Student with this email already exist, cannot have duplicate.");
        }

        // IF NAME IS SHORTER THAN 2 CHARS
        if (student.getName().length() < 2) {
            throw new QueryException("Student name cannot be shorter than 2 chars");
        }

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();
        // CREATE
        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        if (student.getId() == null) {
            throw new QueryException("Student could NOT be saved due unknown reasons.");
        }

        return student.getId();
    }

    // TODO: chef, please fix the issue here. There is no failure scenario.
    // V01 -> JA
    public Long updateName(Long id, String newName) {

        // V02 -> SUCCESS

        // V03 -> 02

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

    // TODO: chef, please fix the issue here. There is no failure scenario.
    //V01->JA
    public Long updateEmail(Long id, String newEmail) {
        // V02 -> 2
        // V01 -> 2
        // V04 -> 0

        // SUCCESS + FAILURE = 2 + 0 = 2
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

    // TODO: chef, please fix the issue here. There is no failure scenario.
    //V01->JA
    public Long updateAge(Long id, Integer newAge) {
        // V02 -> 2
        // V01 -> 2
        // V04 -> 0

        // SUCCESS + FAILURE = 2 + 0 = 2
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

    // TODO: chef, please fix the issue here. There is no failure scenario.

    // V01 -> JA
    public Long delete(Long id) {

        // V02 -> SUCCESS

        // V03 -> 02

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

    // V01 -> JA
    public Long deleteByEmail(String email) throws QueryException {

        // V02 -> SUCCESS | FAILURE

        // V03 -> 1

        // V04 -> 1

        // V05 -> V03 + V04 => 1 + 1 = 2

        Optional<Student> oStudent = findByEmail(email);
        if (oStudent.isPresent()) {
            EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

            // BEGIN TRANSACTION
            manager.getTransaction().begin();

            // REMOVE THE ENTITY
            manager.remove(oStudent.get());

            // SEND EXECUTION REQUEST TO DATABASE SERVER
            manager.getTransaction().commit();

            manager.close();

            return oStudent.get().getId();

        } else {
            throw new QueryException(STUDENT_NOT_FOUND);
        }

    }

    // TODO: voeg hier uitzondering-scenarios toe.

    // V01 -> JA
    public Optional<Student> findById(Long id) {

        // V02 -> SUCCESS

        // V03 -> 1 of 2

        // V05 -> 1 of 2

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        manager.getTransaction().commit();

        Optional<Student> oStudent = Optional.ofNullable(foundStudent);

        manager.close();

        return oStudent;
    }
    // TODO: voeg hier uitzondering-scenarios toe.

    // V01 -> JA
    public Optional<Student> findByEmail(String email) {
    // V02 -> SUCCESS

        // V03 -> 1 of 2

        // V05 -> 1 of 2
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Query query = manager.createNativeQuery("SELECT * FROM student WHERE email LIKE :qpEmail", Student.class)
                .setParameter("qpEmail", email);

        Student foundStudent = (Student) query.getSingleResult();

        manager.getTransaction().commit();

        Optional<Student> oStudent = Optional.ofNullable(foundStudent);

        manager.close();

        return oStudent;
    }

    // TODO: voeg hier uitzondering-scenarios toe.

    // V01 -> JA
    public List<Student> findAll() {

        // V02 -> SUCCESS

        // V03 -> 1

        // V05 -> 1

        List<Student> studentList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery("SELECT * FROM student", Student.class);
        studentList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return studentList;
    }

    // TODO: voeg hier uitzondering-scenarios toe.

    // V01 -> JA
    public List<Student> search(String keyword) {

        // V02 -> SUCCESS

        // V03 -> 1

        // V05 -> 1

        List<Student> studentList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager
                .createNativeQuery("SELECT * FROM student WHERE email LIKE :qpEmail OR naam LIKE :qpName")
                .setParameter("qpEmail", keyword)
                .setParameter("qpName", keyword);


        studentList = query.getResultList();


        manager.getTransaction().commit();
        manager.close();

        return studentList;

    }

    // TODO: voeg hier uitzondering-scenarios toe.

    // V01 -> JA
    public List<Student> search(Integer minAge, Integer maxAge) {

        // V02 -> SUCCESS

        // V03 -> 1

        List<Student> studentList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery("SELECT * FROM student s " +
                "WHERE" +
                " s.leeftijd > " + minAge + " AND s.leeftijd < " + maxAge, Student.class);
        studentList = query.getResultList();


        manager.getTransaction().commit();
        manager.close();

        return studentList;

    }


}
