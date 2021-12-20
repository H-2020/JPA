package be.intec.models;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", nullable = false, unique = true)
    Long photoId;

    // photos must be uploaded
    // photos will be uploaded to the server file system.
    @Column(name = "photo_url")
    String url;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    // Enkele instantie.
    User creator;


}
