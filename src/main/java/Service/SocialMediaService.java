package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

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

    
}
