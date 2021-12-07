package be.intec.repository;

import be.intec.models.Course;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseRepository {


    private static final String COURSE_NOT_FOUND = "Course NOT found!";

    public int save(Course course) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();
        // CREATE
        manager.persist(course);
        manager.getTransaction().commit();
        manager.close();

        return course.getId();
    }

    public Integer updateName(Integer id, String newName) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        // FLAG VARIABLE
        Integer result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Course foundCourse = manager.find(Course.class, id);


        if (foundCourse != null) {

            foundCourse.setName(newName);

            // UPDATE ENTITY
            manager.merge(foundCourse);

            manager.getTransaction().commit();

            result = foundCourse.getId();

        } else {
            System.err.println(COURSE_NOT_FOUND);
            result = -1;
        }

        manager.close();

        return result;
    }


    public Integer updateStartDate(Integer id, LocalDateTime newStartDate) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        Integer result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Course foundCourse = manager.find(Course.class, id);

        if (foundCourse != null) {

            foundCourse.setStartDate(newStartDate);

            // UPDATE ENTITY
            manager.merge(foundCourse);

            manager.getTransaction().commit();

            result = foundCourse.getId();

        } else {
            System.err.println(COURSE_NOT_FOUND);
            result = -1;
        }

        manager.close();

        return result;
    }

    public Integer delete(Integer id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // BEGIN TRANSACTION
        manager.getTransaction().begin();

        // SEARCH ENTITY
        Course foundCourse = manager.find(Course.class, id);

        // IF FOUND
        if (foundCourse!= null) {
            // REMOVE THE ENTITY
            manager.remove(foundCourse);

            // SEND EXECUTION REQUEST TO DATABASE SERVER
            manager.getTransaction().commit();

            manager.close();

            return id;

        } else {

            System.err.println(COURSE_NOT_FOUND);
            return -1;
        }
    }


    public Optional<Course> findById(Integer id) {

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Course foundCourse = manager.find(Course.class, id);

        manager.getTransaction().commit();

        Optional<Course> oCourse = Optional.ofNullable(foundCourse);

        manager.close();

        return oCourse;
    }

    public List<Course> findAll() {

        List<Course> courseList = new ArrayList<>();

        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        Query query = manager.createNativeQuery("SELECT * FROM course", Course.class);
        courseList = query.getResultList();

        manager.getTransaction().commit();

        manager.close();

        return courseList;
    }

    public List<Course> search(String keyword) {


        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // voeg je code hier toe..

        manager.getTransaction().commit();


        manager.close();

        return Collections.emptyList();

    }

}
