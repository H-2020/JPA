package be.intec.models.entities;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class MessageEntity implements Serializable, Comparable<MessageEntity> {

  private Integer id;
  private Integer fromUserId;
  private Integer toUserId;
  private String subject;
  private String content;

      public MessageEntity(){  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void parseAndSetId(String id) {
    this.id = Integer.parseInt(id);
  }

  public MessageEntity withId(Integer id) {
    this.setId(id);
    return this;
  }

  public MessageEntity withParsedId(String id) {
    this.parseAndSetId(id);
    return this;
  }

  public MessageEntity withoutId( Integer id) {
    this.setId(null);
    return this;
  }

  public MessageEntity withoutParsedId(String id) {
    this.setId(null);
    return this;
  }


  public Integer getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(Integer fromUserId) {
    this.fromUserId = fromUserId;
  }

  public void parseAndSetFromUserId(String fromUserId) {
    this.fromUserId = Integer.parseInt(fromUserId);
  }

  public MessageEntity withFromUserId(Integer fromUserId) {
    this.setFromUserId(fromUserId);
    return this;
  }

  public MessageEntity withParsedFromUserId(String fromUserId) {
    this.parseAndSetFromUserId(fromUserId);
    return this;
  }

  public MessageEntity withoutFromUserId( Integer fromUserId) {
    this.setFromUserId(null);
    return this;
  }

  public MessageEntity withoutParsedFromUserId(String fromUserId) {
    this.setFromUserId(null);
    return this;
  }


  public Integer getToUserId() {
    return toUserId;
  }

  public void setToUserId(Integer toUserId) {
    this.toUserId = toUserId;
  }

  public void parseAndSetToUserId(String toUserId) {
    this.toUserId = Integer.parseInt(toUserId);
  }

  public MessageEntity withToUserId(Integer toUserId) {
    this.setToUserId(toUserId);
    return this;
  }

  public MessageEntity withParsedToUserId(String toUserId) {
    this.parseAndSetToUserId(toUserId);
    return this;
  }

  public MessageEntity withoutToUserId( Integer toUserId) {
    this.setToUserId(null);
    return this;
  }

  public MessageEntity withoutParsedToUserId(String toUserId) {
    this.setToUserId(null);
    return this;
  }


  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void parseAndSetSubject(String subject) {
    this.subject = subject;
  }

  public MessageEntity withSubject(String subject) {
    this.setSubject(subject);
    return this;
  }

  public MessageEntity withParsedSubject(String subject) {
    this.parseAndSetSubject(subject);
    return this;
  }

  public MessageEntity withoutSubject( String subject) {
    this.setSubject(null);
    return this;
  }

  public MessageEntity withoutParsedSubject(String subject) {
    this.setSubject(null);
    return this;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void parseAndSetContent(String content) {
    this.content = content;
  }

  public MessageEntity withContent(String content) {
    this.setContent(content);
    return this;
  }

  public MessageEntity withParsedContent(String content) {
    this.parseAndSetContent(content);
    return this;
  }

  public MessageEntity withoutContent( String content) {
    this.setContent(null);
    return this;
  }

  public MessageEntity withoutParsedContent(String content) {
    this.setContent(null);
    return this;
  }


    @Override
    public int compareTo(MessageEntity otherMessage) {
        // define here default comparison criteria 
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MessageEntity)) return false;
        MessageEntity otherMessage = (MessageEntity) obj;
        return 
        this.getId().equals(otherMessage.getId()) 
 &&         this.getFromUserId().equals(otherMessage.getFromUserId()) 
 &&         this.getToUserId().equals(otherMessage.getToUserId()) 
 &&         this.getSubject().equals(otherMessage.getSubject()) 
 &&         this.getContent().equals(otherMessage.getContent()) 
;    }

    @Override
    public int hashCode() {
        return Objects.hash( 
        this.getId()
,         this.getFromUserId()
,         this.getToUserId()
,         this.getSubject()
,         this.getContent()
 );    }

    @Override
    public String toString() {

        return "{ " + 
        "id:" + this.getId()  + ", " + 
        "fromUserId:" + this.getFromUserId()  + ", " + 
        "toUserId:" + this.getToUserId()  + ", " + 
        "subject:" + this.getSubject()  + ", " + 
        "content:" + this.getContent()  + 
    " } ";
    }

    public boolean isNew(){
        return this.getId() == null;
    }

    public boolean isEmpty(){
        return ( this.getId() == null && 
this.getFromUserId() == null && 
this.getToUserId() == null && 
this.getSubject() == null && 
this.getContent() == null );    }

}
