package be.intec.utils;


import be.intec.models.Student;

import javax.persistence.EntityManager;

public class StudentGenerator {

    private StudentGenerator() {

    }

    public static Student saveStudentToDatabase(Student student){

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        student.setId(null);
        manager.getTransaction().begin();

        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        return student;
    }

}
