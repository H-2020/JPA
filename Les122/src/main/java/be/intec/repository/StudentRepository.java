package be.intec.repository;

import be.intec.models.Student;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

public class StudentRepository {

    public Long save(Student student) {

        EntityManager manager = JPAFactory.getEntityManager();

        manager.getTransaction().begin();
        // CREATE
        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        return student.getId();
    }

    public Long updateName(Long id, String newName) {

        EntityManager manager = JPAFactory.getEntityManager();
        // FLAG VARIABLE
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if(foundStudent != null){

            foundStudent.setName(newName);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println("Student NOT found!");
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long updateEmail(Long id, String newEmail) {

        EntityManager manager = JPAFactory.getEntityManager();

        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if(foundStudent != null){

            foundStudent.setEmail(newEmail);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println("Student NOT found!");
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long updateAge(Long id, Integer newAge) {

        EntityManager manager = JPAFactory.getEntityManager();
        Long result = null;

        manager.getTransaction().begin();

        // SEARCH ENTITY
        Student foundStudent = manager.find(Student.class, id);

        if(foundStudent != null){

            foundStudent.setAge(newAge);

            // UPDATE ENTITY
            manager.merge(foundStudent);

            manager.getTransaction().commit();

            result = foundStudent.getId();

        } else {
            System.err.println("Student NOT found!");
            result = -1L;
        }

        manager.close();

        return result;
    }

    public Long delete(Long id) {

        EntityManager manager = JPAFactory.getEntityManager();

        manager.getTransaction().begin();

        // voeg je code hier toe..

        manager.getTransaction().commit();


        manager.close();

        return 0L;
    }

    public Student findById(Long id) {

        EntityManager manager = JPAFactory.getEntityManager();

        manager.getTransaction().begin();

        // voeg je code hier toe..

        manager.getTransaction().commit();


        manager.close();

        return null;

    }

    public List<Student> findAll() {

        EntityManager manager = JPAFactory.getEntityManager();

        manager.getTransaction().begin();

        // voeg je code hier toe..

        manager.getTransaction().commit();


        manager.close();

        return Collections.emptyList();
    }

    public List<Student> search(String keyword) {


        EntityManager manager = JPAFactory.getEntityManager();

        manager.getTransaction().begin();

        // voeg je code hier toe..

        manager.getTransaction().commit();


        manager.close();

        return Collections.emptyList();

    }

}
