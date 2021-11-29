package be.intec.models.entities;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class UserEntity implements Serializable, Comparable<UserEntity> {

  private Integer id;
  private String username;
  private String passcode;
  private String email;

      public UserEntity(){  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void parseAndSetId(String id) {
    this.id = Integer.parseInt(id);
  }

  public UserEntity withId(Integer id) {
    this.setId(id);
    return this;
  }

  public UserEntity withParsedId(String id) {
    this.parseAndSetId(id);
    return this;
  }

  public UserEntity withoutId( Integer id) {
    this.setId(null);
    return this;
  }

  public UserEntity withoutParsedId(String id) {
    this.setId(null);
    return this;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void parseAndSetUsername(String username) {
    this.username = username;
  }

  public UserEntity withUsername(String username) {
    this.setUsername(username);
    return this;
  }

  public UserEntity withParsedUsername(String username) {
    this.parseAndSetUsername(username);
    return this;
  }

  public UserEntity withoutUsername( String username) {
    this.setUsername(null);
    return this;
  }

  public UserEntity withoutParsedUsername(String username) {
    this.setUsername(null);
    return this;
  }


  public String getPasscode() {
    return passcode;
  }

  public void setPasscode(String passcode) {
    this.passcode = passcode;
  }

  public void parseAndSetPasscode(String passcode) {
    this.passcode = passcode;
  }

  public UserEntity withPasscode(String passcode) {
    this.setPasscode(passcode);
    return this;
  }

  public UserEntity withParsedPasscode(String passcode) {
    this.parseAndSetPasscode(passcode);
    return this;
  }

  public UserEntity withoutPasscode( String passcode) {
    this.setPasscode(null);
    return this;
  }

  public UserEntity withoutParsedPasscode(String passcode) {
    this.setPasscode(null);
    return this;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void parseAndSetEmail(String email) {
    this.email = email;
  }

  public UserEntity withEmail(String email) {
    this.setEmail(email);
    return this;
  }

  public UserEntity withParsedEmail(String email) {
    this.parseAndSetEmail(email);
    return this;
  }

  public UserEntity withoutEmail( String email) {
    this.setEmail(null);
    return this;
  }

  public UserEntity withoutParsedEmail(String email) {
    this.setEmail(null);
    return this;
  }


    @Override
    public int compareTo(UserEntity otherUser) {
        // define here default comparison criteria 
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserEntity)) return false;
        UserEntity otherUser = (UserEntity) obj;
        return 
        this.getId().equals(otherUser.getId()) 
 &&         this.getUsername().equals(otherUser.getUsername()) 
 &&         this.getPasscode().equals(otherUser.getPasscode()) 
 &&         this.getEmail().equals(otherUser.getEmail()) 
;    }

    @Override
    public int hashCode() {
        return Objects.hash( 
        this.getId()
,         this.getUsername()
,         this.getPasscode()
,         this.getEmail()
 );    }

    @Override
    public String toString() {

        return "{ " + 
        "id:" + this.getId()  + ", " + 
        "username:" + this.getUsername()  + ", " + 
        "passcode:" + this.getPasscode()  + ", " + 
        "email:" + this.getEmail()  + 
    " } ";
    }

    public boolean isNew(){
        return this.getId() == null;
    }

    public boolean isEmpty(){
        return ( this.getId() == null && 
this.getUsername() == null && 
this.getPasscode() == null && 
this.getEmail() == null );    }

}
