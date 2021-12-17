package be.intec.repository;

import be.intec.exception.QueryException;
import be.intec.models.UserEntity;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.net.CacheRequest;
import java.util.Collections;
import java.util.List;
import static be.intec.exception.ExceptionMessages.*;

public class UserRepository implements IUserRepository<UserEntity,Integer> {



    @Override
    public Integer save(UserEntity userEntity) throws QueryException{

        if (userEntity== null) {
            throw new QueryException(USER_INFO_REQUIRED.getBody());
        }
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();

        // CREATE
        manager.persist(userEntity);

        manager.getTransaction().commit();


        Integer lastSavedId = userEntity.getId();

        manager.close();

        return lastSavedId;
    }

    @Override
    public Integer updateEmail(Integer id, String newEmail) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        UserEntity foundUser = manager.find(UserEntity.class, id);
        foundUser.setEmail(newEmail);

        // UPDATE ENTITY
        manager.merge(foundUser);

        manager.getTransaction().commit();


        Integer lastUpdatedId = foundUser.getId();

        manager.close();

        return lastUpdatedId;
    }

    @Override
    public Integer deleteById(Integer id) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        UserEntity foundUser = manager.find(UserEntity.class, id);



        Integer lastDeletedId = foundUser.getId();

        // REMOVE THE ENTITY
        manager.remove(foundUser);

        manager.getTransaction().commit();


        manager.close();

        return lastDeletedId;
    }

    @Override
    public UserEntity findById(Integer id) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        UserEntity foundUser = manager.find(UserEntity.class, id);
        manager.getTransaction().commit();


        manager.close();


        return foundUser;
    }

    @Override
    public List<UserEntity> findAll() {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        List<UserEntity> userList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM userentity", UserEntity.class)
                        .getResultList());

        manager.getTransaction().commit();
        manager.close();

        return userList;
    }

    @Override
    public List<UserEntity> search(String firstName, String lastName, String email) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        List<UserEntity> studentList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM userentity" +
                                " WHERE email LIKE :qpEmail OR firstname LIKE :qpName OR lastname LIKE :qpLastName", UserEntity.class)
                        .setParameter("qpEmail",email)
                        .setParameter("qpName",firstName)
                        .setParameter("qpLastName",lastName)
                        .getResultList());


        manager.getTransaction().commit();
        manager.close();

        return studentList;

    }

    @Override
    public UserEntity findByEmail(String email) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        UserEntity foundUser = (UserEntity)
                manager.createNativeQuery("SELECT * FROM userentity WHERE email LIKE :qpEmail ", UserEntity.class)
                        .setParameter("qpEmail",email)
                                .getSingleResult();
        manager.getTransaction().commit();


        manager.close();


        return foundUser;
    }

    @Override
    public UserEntity findByPhone(String phone) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        UserEntity foundUser = (UserEntity)
                manager.createNativeQuery("SELECT * FROM userentity WHERE phone LIKE :qpPhone ", UserEntity.class)
                        .setParameter("qpPhone",phone)
                        .getSingleResult();
        manager.getTransaction().commit();


        manager.close();


        return foundUser;
    }
}
