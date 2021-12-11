package be.intec.utils;

import be.intec.models.Student;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class StudentUtils {
    private StudentUtils() {
    }
    public static Student saveStudentToDatabase(Student student) {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        student.setId(null);
        manager.getTransaction().begin();

        manager.persist(student);
        manager.getTransaction().commit();
        manager.close();

        return student;
    }
    public static Student findStudentById(Long id) {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        Student foundStudent = manager.find(Student.class, id);

        manager.close();

        return foundStudent;
    }
    public static Optional<Long> getLastStudentId() {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // READ ALL STUDENT FIRST
        Long lastStudentId = null;
        Student studentWithLastId = null;
        try {
            studentWithLastId = (Student) manager
                    .createNativeQuery("SELECT * from student ORDER BY student_id DESC LIMIT 1", Student.class)
                    .getSingleResult();

        } catch(NoResultException noResultException) {
        }
        if(studentWithLastId != null){
            lastStudentId = studentWithLastId.getId();
        }
        manager.close();


        // GET LAST ELEMENT FROM THE LIST
        // RETURN ID FROM THE LAST ELEMENT
        return Optional.ofNullable(lastStudentId);
    }
}