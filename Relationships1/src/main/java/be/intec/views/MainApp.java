package be.intec.views;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import javax.persistence.EntityManager;

import org.h2.tools.Server;

import be.intec.models.Photo;
import be.intec.models.User;
import be.intec.repository.UserRepo;
import be.intec.utils.JPAFactory;

// RULE: ID cannot be set, must be NULL
// RULE: Must all unique and non-null fields be set

// RULE: READ -> FIND -> transaction.begin() NOT necessary,
// transaction.commit() NOT necessary because we are not changing anything
// RULE: MANIPULATE (PERSIST, MERGE, DELETE) -> transaction must be manually started, and manually ended. 

public class MainApp {

    private static final UserRepo repo = new UserRepo();

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

        repo.save(markZuckerberg);
        System.out.println(markZuckerberg);

        User updatedZuck = repo.update(markZuckerberg.getId(), "chef_hacked_zuck");
        System.out.println(updatedZuck);

        // IF mark.zuck exists -> Updates the USER
        // ELSE mark.zuck does NOT exists -> Creates new USER
        // repo.saveOrUpdate(markZuckerberg);
        // System.out.println(markZuckerberg);

        // repo.saveWithPersist(justinBieber);
        // repo.saveWithMerge(nikolaTesla);

        // System.out.println("Saved with Persist: " + justinBieber);
        // System.out.println("Saved with Merge: " + nikolaTesla);

        // repo.updateWithPersist(justinBieber.getId(), "updatedUNameForJustin");
        // repo.updateWithMerge(nikolaTesla.getId(), "updatedUNameForNikola");

        // System.out.println("Updated with Persist: " + justinBieber);
        // System.out.println("Updated with Merge: " + nikolaTesla);

    }
}
