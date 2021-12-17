package be.intec.repository;

import be.intec.models.MessageEntity;
import be.intec.models.UserEntity;
import be.intec.utils.DatabaseUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageRepositoryTest {

    private final MessageRepository repository = new MessageRepository();
    private static int emailIndex = 0;
    private static int phoneIndex = 0;

    private String nextUniqueEmail() {
        return ("student_message" + (++emailIndex) + "@mail.be");
    }

    private String nextUniquePhone() {
        return ("123" + (++phoneIndex));
    }


    @Test
    void should_save_succeed() {

        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");

        MessageEntity message = new MessageEntity();
        message.setFromUser(user);
        message.setToUser(user2);
        message.setSubject("subject");
        message.setSubject("content");

        Integer savedMessageId = repository.save(message);

//        assertNotNull(savedUserId);
//        assertNotEquals(0, savedUserId);
        Assertions.assertNotNull(savedMessageId);
    }

    @Test
    void should_update_subject_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        MessageEntity message = new MessageEntity();
        UserEntity savedFromUser = DatabaseUtils.saveUserToDatabase(user);
        UserEntity savedToUser = DatabaseUtils.saveUserToDatabase(user2);
        message.setFromUser(savedFromUser);
        message.setToUser(savedToUser);
        message.setSubject("subject");
        message.setSubject("content");



        MessageEntity savedMessage = DatabaseUtils.saveMessageToDatabase(message);

        Integer updatedMessageId = repository.updateSubject(savedMessage.getId(), "newtitel");

        //optional
        assertNotNull(updatedMessageId);
        //must
        assertTrue(updatedMessageId > 0);

    }

    @Test
    void should_update_content_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        MessageEntity message = new MessageEntity();
        UserEntity savedFromUser = DatabaseUtils.saveUserToDatabase(user);
        UserEntity savedToUser = DatabaseUtils.saveUserToDatabase(user2);
        message.setFromUser(savedFromUser);
        message.setToUser(savedToUser);
        message.setSubject("subject");
        message.setSubject("content");


        MessageEntity savedMessage = DatabaseUtils.saveMessageToDatabase(message);

        Integer updatedMessageId = repository.updateContent(savedMessage.getId(), "newcontent");

        //optional
        assertNotNull(updatedMessageId);
        //must
        assertTrue(updatedMessageId > 0);

    }

    @Test
    void should_delete_id_succeed() {

        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        MessageEntity message = new MessageEntity();
        UserEntity savedFromUser = DatabaseUtils.saveUserToDatabase(user);
        UserEntity savedToUser = DatabaseUtils.saveUserToDatabase(user2);
        message.setFromUser(savedFromUser);
        message.setToUser(savedToUser);
        message.setSubject("subject");
        message.setSubject("content");


        MessageEntity savedMessage = DatabaseUtils.saveMessageToDatabase(message);

        Integer deletedMessageId = repository.deleteById(savedMessage.getId());

        //optional
        assertNotNull(deletedMessageId);
        //must
        assertTrue(deletedMessageId > 0);

    }

    @Test
    void should_message_find_by_id_succeed() {

        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        MessageEntity message = new MessageEntity();
        UserEntity savedFromUser = DatabaseUtils.saveUserToDatabase(user);
        UserEntity savedToUser = DatabaseUtils.saveUserToDatabase(user2);
        message.setFromUser(savedFromUser);
        message.setToUser(savedToUser);
        message.setSubject("subject");
        message.setSubject("content");


        MessageEntity savedMessage = DatabaseUtils.saveMessageToDatabase(message);
        MessageEntity findMessageId = repository.findById(savedMessage.getId());


        //optional
        assertNotNull(findMessageId);
        //must
        //assertTrue(findMessageId != null);
        assertEquals(savedMessage.getId(),findMessageId.getId());

    }

    @Test
    void should_find_all_with_limit_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        MessageEntity message = new MessageEntity();
        UserEntity savedFromUser = DatabaseUtils.saveUserToDatabase(user);
        UserEntity savedToUser = DatabaseUtils.saveUserToDatabase(user2);
        message.setFromUser(savedFromUser);
        message.setToUser(savedToUser);
        message.setSubject("subject");
        message.setSubject("content");


        MessageEntity message2 = new MessageEntity();

        message2.setFromUser(user);
        message2.setToUser(user2);
        message2.setSubject("subject2");
        message2.setSubject("content2");


        MessageEntity savedMessage2 = DatabaseUtils.saveMessageToDatabase(message);
        List<MessageEntity> messageList = new ArrayList<>();
        messageList.add(message);
        messageList.add(message2);

        List<MessageEntity> foundMessageList = repository.findAll(1,2);
        assertNotNull(foundMessageList);
        assertTrue(foundMessageList!=null);
    }

    @Test
    void should_find_all_by_sender_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");

        UserEntity sender = DatabaseUtils.saveUserToDatabase(user);
        UserEntity receiver = DatabaseUtils.saveUserToDatabase(user2);

        MessageEntity message = new MessageEntity();
        message.setFromUser(sender);
        message.setToUser(receiver);
        message.setSubject("subject");
        message.setSubject("content");


        MessageEntity savedMessage = DatabaseUtils.saveMessageToDatabase(message);


        List<MessageEntity> foundMessageList = repository.findAllBySender(sender.getId());
        assertNotNull(foundMessageList);
        assertTrue(foundMessageList.size()>0);
    }
    @Test
    void should_find_all_from_receiver_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");

        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        UserEntity sender = DatabaseUtils.saveUserToDatabase(user);
        UserEntity receiver = DatabaseUtils.saveUserToDatabase(user2);

        MessageEntity message = new MessageEntity();
        message.setFromUser(sender);
        message.setToUser(receiver);
        message.setSubject("subject");
        message.setSubject("content");


//        MessageEntity savedMessage = EmailUtils.saveMessageToDatabase(message);
        MessageEntity message2 = new MessageEntity();

        message2.setFromUser(user2);
        message2.setToUser(user);
        message2.setSubject("subject2");
        message2.setSubject("content2");


         MessageEntity savedMessage2 = DatabaseUtils.saveMessageToDatabase(message);
        List<MessageEntity> messageList = new ArrayList<>();
        messageList.add(message);
        messageList.add(message2);

        List<MessageEntity> foundMessageList = repository.findAllFromReceiver(receiver.getId());
        assertNotNull(foundMessageList);
        assertTrue(foundMessageList.size()>0);
    }

}
