package be.intec.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAFactory {

    private JPAFactory() {

    }

    private static EntityManagerFactory factory;

    public static EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("mysql-unit");
        }
        return factory.createEntityManager();
    }

}
