package be.intec.repository;

import be.intec.exception.QueryException;
import be.intec.models.UserEntity;
import be.intec.utils.DatabaseUtils;
import org.junit.jupiter.api.Test;

import static be.intec.exception.ExceptionMessages.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


public class UserRepositoryTest {

    private final UserRepository repository = new UserRepository();

    private static int emailIndex = 0;
    private static int phoneIndex = 0;

    private String nextUniqueEmail() {
        return ("student" + (++emailIndex) + "@mail.be");
    }

    private String nextUniquePhone() {
        return ("0" + (++phoneIndex));
    }

    @Test
    void should_save_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        Integer savedUserId = repository.save(user);

        assertNotNull(savedUserId);
        assertNotEquals(0, savedUserId);
        //Assertions.assertNotNull(savedUserId);
    }
    @Test
    void should_save_fail_when_user_is_null() {

        Exception exception = assertThrows(
                QueryException.class,
                () -> {
                    repository.save(null);
                });

        String expectedMessage = USER_INFO_REQUIRED.getBody();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void should_update_email_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        UserEntity savedUser = DatabaseUtils.saveUserToDatabase(user);

        Integer updatedUserId = repository.updateEmail(savedUser.getId(), "mynewmail@intec.be");

        //optional
        assertNotNull(updatedUserId);
        //must
        assertTrue(updatedUserId > 0);

    }

    @Test
    void should_delete_id_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        UserEntity savedUser = DatabaseUtils.saveUserToDatabase(user);
        Integer deletedUserId = repository.deleteById(savedUser.getId());

        //optional
        assertNotNull(deletedUserId);
        //must
        assertTrue(deletedUserId > 0);

    }

    @Test
    void should_student_find_by_id_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        UserEntity savedUser = DatabaseUtils.saveUserToDatabase(user);

        UserEntity findUserId = repository.findById(savedUser.getId());


        //optional
        assertNotNull(findUserId);
        //must
        assertTrue(findUserId != null);

    }

    @Test
    void should_student_find_all_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
//        UserEntity savedUser = EmailUtils.saveUserToDatabase(user);
        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        //UserEntity savedUser2 = EmailUtils.saveUserToDatabase(user);

        List<UserEntity> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);
        List<UserEntity> findUserList = repository.findAll();
        assertNotNull(findUserList);
        assertTrue(findUserList != null);


    }
    @Test
    void should_student_find_by_email_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        UserEntity savedUser = DatabaseUtils.saveUserToDatabase(user);

       UserEntity findUserEmail = repository.findByEmail(savedUser.getEmail());


        //optional
        assertNotNull(findUserEmail);
        //must
       assertTrue(findUserEmail != null);


    }

    @Test
    void should_search_with_keyword_all_succeed() {
        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
//        UserEntity savedUser = EmailUtils.saveUserToDatabase(user);
        UserEntity user2 = new UserEntity();
        user2.setFirstName("Nikola");
        user2.setLastName("Tesla");
        user2.setEmail(nextUniqueEmail());
        user2.setPhone(nextUniquePhone());
        user2.setPasscode("5463");
        //UserEntity savedUser2 = EmailUtils.saveUserToDatabase(user);

        List<UserEntity> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);
        List<UserEntity> searchUserList = repository.search("Justin", "Bieber", "mail@intec");
        assertNotNull(searchUserList);
        assertTrue(searchUserList != null);
    }

    @Test
    void should_student_find_by_phone_succeed() {


        UserEntity user = new UserEntity();
        user.setFirstName("Justin");
        user.setLastName("Bieber");
        user.setEmail(nextUniqueEmail());
        user.setPhone(nextUniquePhone());
        user.setPasscode("1234");
        UserEntity savedUser = DatabaseUtils.saveUserToDatabase(user);

        UserEntity findUserPhone = repository.findByPhone(savedUser.getPhone());


        //optional
        assertNotNull(findUserPhone);
        //must
        assertEquals(savedUser.getPhone(),findUserPhone.getPhone());

    }

}
