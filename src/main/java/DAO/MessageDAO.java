package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

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
}
