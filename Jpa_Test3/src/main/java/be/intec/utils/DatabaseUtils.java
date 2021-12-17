package be.intec.utils;

import be.intec.models.MessageEntity;
import be.intec.models.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class DatabaseUtils {


    private DatabaseUtils() {
    }

    public static UserEntity saveUserToDatabase(UserEntity userEntity) {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        userEntity.setId(null);
        manager.getTransaction().begin();

        manager.persist(userEntity);
        manager.getTransaction().commit();
        manager.close();

        return userEntity;
    }
    public static MessageEntity saveMessageToDatabase(MessageEntity messageEntity) {

        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        messageEntity.setId(null);
        manager.getTransaction().begin();

        manager.persist(messageEntity);
        manager.getTransaction().commit();
        manager.close();

        return messageEntity;
    }

    public static UserEntity findById(Integer id) {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        UserEntity foundUser = manager.find(UserEntity.class, id);

        manager.close();

        return foundUser;
    }

    public static Optional<Integer> getLastUserId() {
        final EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        // READ ALL STUDENT FIRST
        Integer lastUserId = null;
        UserEntity userEntityWithLastId = null;
        try {
            userEntityWithLastId = (UserEntity) manager
                    .createNativeQuery("SELECT * from userentity ORDER BY user_id DESC LIMIT 1", UserEntity.class)
                    .getSingleResult();

        } catch (NoResultException noResultException) {
        }
        if (userEntityWithLastId != null) {
            lastUserId = userEntityWithLastId.getId();
        }
        manager.close();


        // GET LAST ELEMENT FROM THE LIST
        // RETURN ID FROM THE LAST ELEMENT
        return Optional.ofNullable(lastUserId);
    }
}


