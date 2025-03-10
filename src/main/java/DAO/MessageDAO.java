package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

//import org.junit.runners.model.Statement;

public class MessageDAO {
    public boolean messagerReal(Message message) {
        String sql = "SELECT * FROM Account WHERE account_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, message.getPosted_by());

            ResultSet rs = ps.executeQuery();
            return rs.next();
         
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public Message createMessage(Message message) {
        String sql ="INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        long time = System.currentTimeMillis() / 1000;

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, time);

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedMessageId = generatedKeys.getInt(1);
                    return new Message(generatedMessageId , message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }

            //return message;
         
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        String sql = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("message_id");
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    Long timePosted = rs.getLong("time_posted_epoch");
                  
                    Message message = new Message(id, postedBy, messageText, timePosted);
                    messages.add(message);

                    
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;

    }

    public Message getMessageById(int id) throws SQLException {
        String sql = "SELECT * FROM Message WHERE message_id = ?";

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    long timePosted = rs.getLong("time_posted_epoch");

                    return new Message(id, postedBy, messageText, timePosted);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Message deleteMessageById(int id) throws SQLException {   
        String selectSql = "SELECT * FROM Message WHERE message_id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    long timePosted = rs.getLong("time_posted_epoch");
                    
                    Message messageToDelete = new Message(id, postedBy, messageText, timePosted);
                    
                    String deleteSql = "DELETE FROM Message WHERE message_id = ?";
                    try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                        deletePs.setInt(1, id);
                        if (deletePs.executeUpdate() > 0) {
                            return messageToDelete;
                        }
                    }
                }
            }
        }
        return null; 
    }

    public Message updateMessageById(int id, String newMessageText) throws SQLException {
        String selectSql = "SELECT * FROM Message WHERE message_id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
            
            selectPs.setInt(1, id);
            
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    int postedBy = rs.getInt("posted_by");
                    long timePosted = rs.getLong("time_posted_epoch");
                    
                    String updateSql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setString(1, newMessageText);
                        updatePs.setInt(2, id);
                        
                        int rowsAffected = updatePs.executeUpdate();
                        if (rowsAffected > 0) {
                            return new Message(id, postedBy, newMessageText, timePosted);
                        }
                    }
                }
            }
        }
        
        return null; 
    }
    
    public List<Message> getAllMessageForUser(int id) throws SQLException {
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        List<Message> messages = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int messageId = rs.getInt("message_id");
                    String messageText = rs.getString("message_text");
                    long timePosted = rs.getLong("time_posted_epoch");
                    Message message = new Message(messageId, id, messageText, timePosted);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return messages;
    }
    
}
