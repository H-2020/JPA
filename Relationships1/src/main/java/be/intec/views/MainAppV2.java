package be.intec.views;

import be.intec.models.Photo;
import be.intec.models.User;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainAppV2 {

    public static void main(String[] args) {

        User justinBieber = new User();
        justinBieber.setUsername("just.in");
        justinBieber.setPasscode("iAmRich");
        justinBieber.setDateOfBirth(LocalDate.of(2011, 3, 22));

        User nikolaTesla = new User();
        nikolaTesla.setUsername("niko.la");
        nikolaTesla.setPasscode("N!k0l@");
        nikolaTesla.setDateOfBirth(LocalDate.of(1952, 3, 11));

        User markZuckerberg = new User();
        markZuckerberg.setUsername("mark.zuck");
        markZuckerberg.setPasscode("123");
        markZuckerberg.setDateOfBirth(LocalDate.of(1986, 3, 11));

        Photo photo01=new Photo();
        photo01.setCreator(justinBieber);
        photo01.setCreatedAt(LocalDateTime.now());


        EntityManager em = JPAFactory.getEntityManagerFactory().createEntityManager();

        em.getTransaction().begin();

        // CREATE OR UPDATE

        em.persist(justinBieber);
        em.persist(nikolaTesla);
        em.persist(markZuckerberg);

        em.getTransaction().commit();

        em.close();

    }

}
