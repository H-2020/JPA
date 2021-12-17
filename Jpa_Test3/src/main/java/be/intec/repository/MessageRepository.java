package be.intec.repository;

import be.intec.models.MessageEntity;
import be.intec.models.UserEntity;
import be.intec.utils.JPAFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MessageRepository implements IMessageRepository<MessageEntity,Integer>{

    @Override
    public Integer save(MessageEntity messageEntity) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();

        // CREATE
        manager.persist(messageEntity.getFromUser());
        manager.persist(messageEntity.getToUser());
        manager.persist(messageEntity);

        manager.getTransaction().commit();


        Integer lastSavedId = messageEntity.getId();

        manager.close();

        return lastSavedId;
    }

    @Override
    public Integer updateSubject(Integer id, String content) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        MessageEntity foundSubject = manager.find(MessageEntity.class, id);
        foundSubject.setSubject(content);

        // UPDATE ENTITY
        manager.merge(foundSubject);

        manager.getTransaction().commit();


        Integer lastUpdatedId = foundSubject.getId();

        manager.close();

        return lastUpdatedId;
    }

    @Override
    public Integer updateContent(Integer id, String content) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        MessageEntity foundContent = manager.find(MessageEntity.class, id);
        foundContent.setContent(content);

        // UPDATE ENTITY
        manager.merge(foundContent);

        manager.getTransaction().commit();


        Integer lastUpdatedId = foundContent.getId();

        manager.close();

        return lastUpdatedId;
    }

    @Override
    public Integer deleteById(Integer id) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        manager.getTransaction().begin();

        // SEARCH ENTITY
        MessageEntity foundMessage = manager.find(MessageEntity.class, id);



        Integer lastDeletedId = foundMessage.getId();

        // REMOVE THE ENTITY
        manager.remove(foundMessage);

        manager.getTransaction().commit();


        manager.close();

        return lastDeletedId;
    }

    @Override
    public MessageEntity findById(Integer id) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        MessageEntity foundMessage = manager.find(MessageEntity.class,id);
        manager.getTransaction().commit();


        manager.close();


        return foundMessage;
    }



    @Override
    public List<MessageEntity> findAll(int limit, int offset) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<MessageEntity> messageEntityList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM messageentity LIMIT :qpLimit OFFSET :qpOffset", MessageEntity.class)
                        .setParameter("qpLimit", limit)
                        .setParameter("qpOffset", offset)
                        .getResultList());

        manager.close();

        return messageEntityList;
    }

    @Override
    public List<MessageEntity> findAllBySender(Integer fromUser) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<MessageEntity> messageEntityList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM messageentity WHERE from_user_id =:qpFromUser", MessageEntity.class)
                        .setParameter("qpFromUser", fromUser)
                         .getResultList());

        manager.close();

        return messageEntityList;
    }

    @Override
    public List<MessageEntity> findAllFromReceiver(Integer toUser) {
        EntityManager manager = JPAFactory.getEntityManagerFactory().createEntityManager();

        List<MessageEntity> messageEntityList = Collections.unmodifiableList(
                manager.createNativeQuery("SELECT * FROM messageentity WHERE to_user_id =:qpToUser", MessageEntity.class)
                        .setParameter("qpToUser", toUser)
                        .getResultList());

        manager.close();

        return messageEntityList;
    }
}
