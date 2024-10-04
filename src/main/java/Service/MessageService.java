package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    // Create
   public Message createMessage(Message message) {
    if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
        return null; // Invalid message text
    }
    
    Account user = accountDAO.getAccountById(message.getPosted_by());
    if (user == null) {
        return null; // User does not exist
    }

    return messageDAO.createMessage(message);
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId); 
    }
    


  public Message deleteMessage(int messageId) {
    Message messageToDelete = messageDAO.getMessageById(messageId);
    if (messageToDelete == null) {
        return null;
    }
 
    return messageDAO.deleteMessage(messageId);
}

    
    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }
        boolean isUpdated = messageDAO.updateMessage(messageId, newText);
        if (isUpdated) {
            return messageDAO.getMessageById(messageId); 
        }
        return null;
    }

     public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);  
    }
}
