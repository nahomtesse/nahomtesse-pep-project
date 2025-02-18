package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account createAccount(Account account) {
        String sql ="INSERT INTO Account (username, password) VALUES (?, ?)";

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedAccountId = generatedKeys.getInt(1);
                    return new Account(generatedAccountId, account.getUsername(), account.getPassword());
                }
            }

            //return account;
         
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean nameExists (Account account) {
        String sql = "SELECT * FROM Account WHERE username = ?";

        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, account.getUsername());

            ResultSet rs = ps.executeQuery();
            return rs.next();
         
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
        
    }

}