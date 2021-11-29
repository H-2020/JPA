package be.intec.repositories;

import be.intec.models.entities.*;
import java.sql.*;
import java.util.*;
import be.intec.services.exceptions.*;

public class UserRepository {

    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet results = null;
    
    private Connection getConnection() throws SQLException {
            Connection conn;
            conn = ConnectionFactory.getInstance().getConnection();
            return conn;
    }
    
    public int clear() {

    int rowsDeleted = 0;
    final var entities = findAll();
        for ( final UserEntity entity : entities ) {
            rowsDeleted += deleteById( entity.getId() );
        }
        
        return rowsDeleted;
    }
    
    
    public int resetSequence() {
    
        int rowsEffected = 0;
    
        try {
            String query = "ALTER TABLE user AUTO_INCREMENT = 0";
            connection = getConnection();
            statement = connection.prepareStatement( query );
            rowsEffected = statement.executeUpdate();

        } catch ( SQLException sqlException ) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if ( statement != null ) {
                    statement.close();
                }
                if ( connection != null ) {
                    connection.close();
                }
            } catch ( Exception exception ) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    
    }
    
    
    public int save( UserEntity entityToSave ) throws UserException {
    
        int rowsEffected = 0;
    
        try {
            String query = "insert into user ( username, passcode, email ) values ( ?, ?, ? )";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, entityToSave.getUsername());
            statement.setString(2, entityToSave.getPasscode());
            statement.setString(3, entityToSave.getEmail());

             rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int saveAll( List<UserEntity> entitiesToSave ) throws UserException {
        int noOfRowsCreated = 0;
        for( UserEntity entity : entitiesToSave){ 
             noOfRowsCreated += save(entity); 
        } 
       return noOfRowsCreated;
    }


    public UserEntity findById( Integer id ) throws UserException {


        if( id <= 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        UserEntity item = new UserEntity();

        try {
            String query = "select id, username, passcode, email from user where id = ?";
            connection = getConnection();

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            results = statement.executeQuery();
            if(results.next()){
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );
            }
        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return item;
    }

    public List<UserEntity> findAllByExample( UserEntity entityToSearch ) throws UserException {

        final List<UserEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, username, passcode, email from user where username = ? OR passcode = ? OR email = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, entityToSearch.getUsername() );
            statement.setString( 2, entityToSearch.getPasscode() );
            statement.setString( 3, entityToSearch.getEmail() );

            results = statement.executeQuery();
            while(results.next()){
            UserEntity item = new UserEntity();
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return itemList;
    }

    public List<UserEntity> findAll() throws UserException {

        final List<UserEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, username, passcode, email from user ";
            connection = getConnection();
            statement = connection.prepareStatement(query);

            results = statement.executeQuery();
            while(results.next()){
                UserEntity item = new UserEntity();
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return itemList;
    }

    public int updateById( Integer id, UserEntity entityToUpdate ) throws UserException {

        if( id <= 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        if ( entityToUpdate == null ) {
            throw new UserException( " User is required." ).nullUserException();
        }

        int rowsEffected = 0;

        try {
            String query = "update user set   username  = ?,   passcode  = ?,   email  = ? where id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, entityToUpdate.getUsername());
            statement.setString(2, entityToUpdate.getPasscode());
            statement.setString(3, entityToUpdate.getEmail());
            statement.setInt( 4, id );

            rowsEffected = statement.executeUpdate();

        } catch ( SQLException sqlException ) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }

    public int deleteById( Integer id ) throws UserException {


        if( id < 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        int rowsEffected = 0;

        try {
            String query = "delete from user where id = ? ";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            rowsEffected = statement.executeUpdate();
        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }

    public int updateUsernameById( Integer id, String username ) throws UserException {

        if( id < 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        if ( username == null ) {
            throw new UserException( " username is required." ).nullUserException();
        }

        int rowsEffected = 0;

        try {
            String query = "update user set  username  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, username );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int updatePasscodeById( Integer id, String passcode ) throws UserException {

        if( id < 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        if ( passcode == null ) {
            throw new UserException( " passcode is required." ).nullUserException();
        }

        int rowsEffected = 0;

        try {
            String query = "update user set  passcode  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, passcode );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int updateEmailById( Integer id, String email ) throws UserException {

        if( id < 0 ) {
            throw new UserException( " User ID is required." ).requiredFields("id");
        }

        if ( email == null ) {
            throw new UserException( " email is required." ).nullUserException();
        }

        int rowsEffected = 0;

        try {
            String query = "update user set  email  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, email );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return rowsEffected;
    }



    public boolean existsById( Integer id ) throws UserException {

        boolean exists = false;
        try {
            String query = "select id, username, passcode, email from user where id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, id );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<UserEntity> searchByUsername( String username ) throws UserException {

        final List<UserEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, username, passcode, email from user where username LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, username );

            results = statement.executeQuery();

            while( results.next() ) {
            UserEntity item = new UserEntity();
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByUsername( String username ) throws UserException {

        boolean exists = false;
        try {
            String query = "select id, username, passcode, email from user where username LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, username );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<UserEntity> searchByPasscode( String passcode ) throws UserException {

        final List<UserEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, username, passcode, email from user where passcode LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, passcode );

            results = statement.executeQuery();

            while( results.next() ) {
            UserEntity item = new UserEntity();
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByPasscode( String passcode ) throws UserException {

        boolean exists = false;
        try {
            String query = "select id, username, passcode, email from user where passcode LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, passcode );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<UserEntity> searchByEmail( String email ) throws UserException {

        final List<UserEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, username, passcode, email from user where email LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, email );

            results = statement.executeQuery();

            while( results.next() ) {
            UserEntity item = new UserEntity();
                item.setId( results.getInt("id") );
                item.setUsername( results.getString("username") );
                item.setPasscode( results.getString("passcode") );
                item.setEmail( results.getString("email") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByEmail( String email ) throws UserException {

        boolean exists = false;
        try {
            String query = "select id, username, passcode, email from user where email LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, email );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new UserException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
        }

        return exists;
    }


}
