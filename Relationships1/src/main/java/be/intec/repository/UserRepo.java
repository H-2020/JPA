package be.intec.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import be.intec.models.User;
import be.intec.utils.JPAFactory;

public class UserRepo {

    public void save(User user) {

        EntityManager entityManager1 = JPAFactory.getEntityManagerFactory().createEntityManager();

        entityManager1.getTransaction().begin();

        // CREATE OR UPDATE
        entityManager1.persist(user);

        entityManager1.getTransaction().commit();

        entityManager1.close();
    }

    public User update(Long userId, String username) throws PersistenceException {

        EntityManager entityManager2 = JPAFactory.getEntityManagerFactory().createEntityManager();

        entityManager2.getTransaction().begin();

        // ZOEK DE ENTITEIT
        User user = entityManager2.find(User.class, userId);

        // ALS HET GEBRUIKER STAAT NIET OP CONTEXT
        if (user == null) {
            // THROW EXCEPTION
            throw new PersistenceException("User to update with this ID not found!");
        }

        user.setUsername(username);

        User updatedEntity = entityManager2.merge(user);

        entityManager2.getTransaction().commit();

        entityManager2.close();

        return updatedEntity;
    }

    /*
     * public void addToBasket(Product product) {
     * 
     * // IF PRODUCT EXISTS -> UPDATE QUANTITY FROM THE BASKET
     * // IF NOT -> CREATE NEW PRODUCT IN THE BASKET
     * em.persist(product);
     * }
     */

    /*
     * public void saveWithMerge(User user) {
     * 
     * EntityManager entityManager1 =
     * JPAFactory.getEntityManagerFactory().createEntityManager();
     * 
     * entityManager1.getTransaction().begin();
     * 
     * // UPDATE
     * entityManager1.merge(user);
     * 
     * entityManager1.getTransaction().commit();
     * 
     * entityManager1.close();
     * }
     */

    /*
     * public void updateWithPersist(Long userId, String username) {
     * 
     * EntityManager entityManager2 =
     * JPAFactory.getEntityManagerFactory().createEntityManager();
     * 
     * entityManager2.getTransaction().begin();
     * 
     * // ZOEK DE ENTITEIT
     * User user = entityManager2.find(User.class, userId);
     * 
     * user.setUsername(username);
     * 
     * // ALS NIET NULL
     * if (user != null) {
     * // UPDATE DE ENTITEIT
     * entityManager2.persist(user);
     * }
     * 
     * entityManager2.getTransaction().commit();
     * 
     * entityManager2.close();
     * 
     * }
     */

    /*
     * public void updateWithMerge(Long userId, String username) {
     * 
     * EntityManager entityManager2 =
     * JPAFactory.getEntityManagerFactory().createEntityManager();
     * 
     * entityManager2.getTransaction().begin();
     * 
     * // ZOEK DE ENTITEIT
     * User user = entityManager2.find(User.class, userId);
     * 
     * user.setUsername(username);
     * 
     * // ALS NIET NULL
     * if (user != null) {
     * // UPDATE DE ENTITEIT
     * entityManager2.merge(user);
     * }
     * 
     * entityManager2.getTransaction().commit();
     * 
     * entityManager2.close();
     * 
     * }
     */

    // REPO CODE

    // OPLOSSINGEN VAN DE OPDRACHT

}
