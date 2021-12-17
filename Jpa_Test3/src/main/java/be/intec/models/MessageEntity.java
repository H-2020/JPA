package be.intec.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter

public class MessageEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;
   @ManyToOne
   @JoinColumn(name = "to_user_id")
    private UserEntity toUser;
    private String subject;
    private String content;
//    @ManyToOne
//    UserEntity user;



}
