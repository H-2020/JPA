package be.intec.repository;

import be.intec.models.Exam;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ExamRepository {

    private static final String EXAM_NOT_FOUND = "Exam NOT found!";

    public Long save(Exam exam) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();
        // CREATE
        manager.persist(exam);
        manager.getTransaction().commit();
        manager.close();

        return exam.getId();
    }

    public Long updateName(Long id, String newName) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        // FLAG VARIABLE
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Exam foundExam = manager.find(Exam.class, id);


        if (foundExam != null) {

            foundExam.setName(newName);

            // UPDATE ENTITY
            manager.merge(foundExam);

            manager.getTransaction().commit();

            result = foundExam.getId();

        } else {
            System.err.println(EXAM_NOT_FOUND);
            result = -1L;
        }

        manager.close();

        return result;
    }


    public Long updateExamDate(Long id, LocalDateTime newExamDate) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Exam foundExam = manager.find(Exam.class, id);

        if (foundExam != null) {

            foundExam.setExamDate(newExamDate);

            // UPDATE ENTITY
            manager.merge(foundExam);

            manager.getTransaction().commit();

            result = foundExam.getId();

        } else {
            System.err.println(EXAM_NOT_FOUND);
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
        Exam foundExam = manager.find(Exam.class, id);

        // IF FOUND
        if (foundExam != null) {
            // REMOVE THE ENTITY
            manager.remove(foundExam);

            // SEND EXECUTION REQUEST TO DATABASE SERVER
            manager.getTransaction().commit();

            manager.close();

            return id;

        } else {

            System.err.println(EXAM_NOT_FOUND);
            return -1L;
        }
    }

    // RETURN NEVER NEVER NEVER NULL, AND NEVER SAY NEVER :)
    public Optional<Exam> findById(Long id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Exam foundExam = manager.find(Exam.class, id);

        manager.getTransaction().commit();

        Optional<Exam> oExam = Optional.ofNullable(foundExam);

        manager.close();

        return oExam;
    }

    public List<Exam> findAll() {

        List<Exam> examList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery("SELECT * FROM exam", Exam.class);
        examList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return examList;
    }

    public List<Exam> search(String keyword) {


        List<Exam> examList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery(("SELECT * FROM exam e " +
                        "WHERE e.naam LIKE '%" + keyword + "%'" +
                        "OR e.examDate LIKE '%" + keyword + "%'"), Exam.class);
        studentList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return examList;


    }

}



