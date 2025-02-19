package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.*;

public class SocialMediaService {
    private AccountDAO accountDAO = new AccountDAO();
    private MessageDAO messageDAO = new MessageDAO();

    public Account registration(Account account) {
        String pass = account.getPassword();
        int passLength = pass.length();

        if (account.getUsername() == null || account.getUsername() == "" || passLength < 4 
            || accountDAO.nameExists(account)) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account logginIn(Account loginAccount) {
        Account account = accountDAO.getAccountByUsername(loginAccount);

        if (account != null && account.getPassword().equals(loginAccount.getPassword())) {
            return account;
        }

        return null;
    }

    public Message messaging(Message message) {
        String mess = message.getMessage_text();
        int messLength = mess.length();

        if (message.getMessage_text() == null || message.getMessage_text() == "" || messLength > 255
            || !messageDAO.messagerReal(message)) {
            return null;
        }

        return messageDAO.createMessage(message);
    }

    public List<Message> allMessage() {
        List<Message> messages;
        try {
            messages = messageDAO.getAllMessages();

            if (messages == null) {
                return null;
            }
    
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
        
    }

    public Message getMessageById(int id) {
        Message message;
        try {
            message = messageDAO.getMessageById(id);

            return message;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message deleteMessageById(int id) {
        Message message;
        try {
            message = messageDAO.deleteMessageById(id);
            return message;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message updateMessageById(int id, String newMessageText) {
        if (newMessageText == null || newMessageText == "" || newMessageText.length() > 255) {
            return null;
        }
    
        try {
            Message updatedMessage = messageDAO.updateMessageById(id, newMessageText);
            return updatedMessage;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    
}
