package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    // Register a new account with validation
    public Account createAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null; // Username cannot be blank
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; // Password 4 characters long
        }

        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            return null; // Username already exists
        }
        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        Account user = accountDAO.getAccountByUsername(username); 
        if (user != null && user.getPassword().equals(password)) {
            return user; 
        }
        return null; 
    }
    
    
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId); // Fetch account from DAO
    }
    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }
}
