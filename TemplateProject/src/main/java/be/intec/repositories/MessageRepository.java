package be.intec.repositories;

import be.intec.models.entities.*;
import java.sql.*;
import java.util.*;
import be.intec.services.exceptions.*;

public class MessageRepository {

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
        for ( final MessageEntity entity : entities ) {
            rowsDeleted += deleteById( entity.getId() );
        }
        
        return rowsDeleted;
    }
    
    
    public int resetSequence() {
    
        int rowsEffected = 0;
    
        try {
            String query = "ALTER TABLE message AUTO_INCREMENT = 0";
            connection = getConnection();
            statement = connection.prepareStatement( query );
            rowsEffected = statement.executeUpdate();

        } catch ( SQLException sqlException ) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if ( statement != null ) {
                    statement.close();
                }
                if ( connection != null ) {
                    connection.close();
                }
            } catch ( Exception exception ) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    
    }
    
    
    public int save( MessageEntity entityToSave ) throws MessageException {
    
        int rowsEffected = 0;
    
        try {
            String query = "insert into message ( from_user_id, to_user_id, subject, content ) values ( ?, ?, ?, ? )";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, entityToSave.getFromUserId());
            statement.setInt(2, entityToSave.getToUserId());
            statement.setString(3, entityToSave.getSubject());
            statement.setString(4, entityToSave.getContent());

             rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int saveAll( List<MessageEntity> entitiesToSave ) throws MessageException {
        int noOfRowsCreated = 0;
        for( MessageEntity entity : entitiesToSave){ 
             noOfRowsCreated += save(entity); 
        } 
         
    }


    public MessageEntity findById( Integer id ) throws MessageException {


        if( id <= 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        MessageEntity item = new MessageEntity();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where id = ?";
            connection = getConnection();

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            results = statement.executeQuery();
            if(results.next()){
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );
            }
        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return item;
    }

    public List<MessageEntity> findAllByExample( MessageEntity entityToSearch ) throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where from_user_id = ? OR to_user_id = ? OR subject = ? OR content = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, entityToSearch.getFromUserId() );
            statement.setInt( 2, entityToSearch.getToUserId() );
            statement.setString( 3, entityToSearch.getSubject() );
            statement.setString( 4, entityToSearch.getContent() );

            results = statement.executeQuery();
            while(results.next()){
            MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }

    public List<MessageEntity> findAll() throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message ";
            connection = getConnection();
            statement = connection.prepareStatement(query);

            results = statement.executeQuery();
            while(results.next()){
                MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }

    public int updateById( Integer id, MessageEntity entityToUpdate ) throws MessageException {

        if( id <= 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        if ( entityToUpdate == null ) {
            throw new MessageException( " Message is required." ).nullMessageException();
        }

        int rowsEffected = 0;

        try {
            String query = "update message set   from_user_id  = ?,   to_user_id  = ?,   subject  = ?,   content  = ? where id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, entityToUpdate.getFromUserId());
            statement.setInt(2, entityToUpdate.getToUserId());
            statement.setString(3, entityToUpdate.getSubject());
            statement.setString(4, entityToUpdate.getContent());
            statement.setInt( 5, id );

            rowsEffected = statement.executeUpdate();

        } catch ( SQLException sqlException ) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }

    public int deleteById( Integer id ) throws MessageException {


        if( id < 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        int rowsEffected = 0;

        try {
            String query = "delete from message where id = ? ";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            rowsEffected = statement.executeUpdate();
        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }

    public int updateFromUserIdById( Integer id, Integer fromUserId ) throws MessageException {

        if( id < 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        if ( fromUserId == null ) {
            throw new MessageException( " fromUserId is required." ).nullMessageException();
        }

        int rowsEffected = 0;

        try {
            String query = "update message set  from_user_id  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, fromUserId );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int updateToUserIdById( Integer id, Integer toUserId ) throws MessageException {

        if( id < 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        if ( toUserId == null ) {
            throw new MessageException( " toUserId is required." ).nullMessageException();
        }

        int rowsEffected = 0;

        try {
            String query = "update message set  to_user_id  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, toUserId );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int updateSubjectById( Integer id, String subject ) throws MessageException {

        if( id < 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        if ( subject == null ) {
            throw new MessageException( " subject is required." ).nullMessageException();
        }

        int rowsEffected = 0;

        try {
            String query = "update message set  subject  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, subject );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }


    public int updateContentById( Integer id, String content ) throws MessageException {

        if( id < 0 ) {
            throw new MessageException( " Message ID is required." ).requiredFields("id");
        }

        if ( content == null ) {
            throw new MessageException( " content is required." ).nullMessageException();
        }

        int rowsEffected = 0;

        try {
            String query = "update message set  content  = ? where id = ?";

            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, content );
            statement.setInt( 2, id );

            rowsEffected = statement.executeUpdate();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return rowsEffected;
    }



    public boolean existsById( Integer id ) throws MessageException {

        boolean exists = false;
        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, id );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<MessageEntity> searchByFromUserId( Integer fromUserId ) throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where from_user_id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, fromUserId );

            results = statement.executeQuery();

            while( results.next() ) {
            MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByFromUserId( Integer fromUserId ) throws MessageException {

        boolean exists = false;
        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where from_user_id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, fromUserId );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<MessageEntity> searchByToUserId( Integer toUserId ) throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where to_user_id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, toUserId );

            results = statement.executeQuery();

            while( results.next() ) {
            MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByToUserId( Integer toUserId ) throws MessageException {

        boolean exists = false;
        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where to_user_id = ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt( 1, toUserId );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<MessageEntity> searchBySubject( String subject ) throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where subject LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, subject );

            results = statement.executeQuery();

            while( results.next() ) {
            MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsBySubject( String subject ) throws MessageException {

        boolean exists = false;
        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where subject LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, subject );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return exists;
    }

    public List<MessageEntity> searchByContent( String content ) throws MessageException {

        final List<MessageEntity> itemList = new ArrayList<>();

        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where content LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, content );

            results = statement.executeQuery();

            while( results.next() ) {
            MessageEntity item = new MessageEntity();
                item.setId( results.getInt("id") );
                item.setFromUserId( results.getInt("from_user_id") );
                item.setToUserId( results.getInt("to_user_id") );
                item.setSubject( results.getString("subject") );
                item.setContent( results.getString("content") );

                itemList.add(item);
            }

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return itemList;
    }


    public boolean existsByContent( String content ) throws MessageException {

        boolean exists = false;
        try {
            String query = "select id, from_user_id, to_user_id, subject, content from message where content LIKE ?";
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString( 1, content );

            results = statement.executeQuery();
            exists = results.next();

        } catch (SQLException sqlException) {
                throw new MessageException(sqlException.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception exception) {
                throw new MessageException(exception.getMessage());
            }
        }

        return exists;
    }


}
