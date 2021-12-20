package be.intec.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    Long id;

    @Column(name = "username")
    String username;

    @Column(name = "passcode")
    String passcode;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

   @OneToMany
    @JoinTable(name="shares",joinColumns = @JoinColumn(name="creator_id"),inverseJoinColumns = @JoinColumn(name="shared_photo_id"))
    List<Photo> shares;

   @ManyToMany
   @JoinTable(name="views",joinColumns = @JoinColumn(name="viewer_id"),inverseJoinColumns = @JoinColumn(name="viewed_photo_id"))
    List<Photo> views;

}
