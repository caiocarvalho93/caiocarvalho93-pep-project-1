package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Message;
import Service.MessageService;
import Model.Account;
import Service.AccountService;


import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final MessageService messageService = new MessageService();
    private final AccountService accountService = new AccountService();
    

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
            // Message endpoints
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageByIdHandler);  //javelin, cant connect without the brackets on id
        app.delete("/messages/{id}", this::deleteMessageHandler);
        app.patch("/messages/{id}", this::updateMessageHandler);
        app.get("/accounts/{id}/messages", this::getMessagesByUserHandler); 

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        
        return app;
    }

    // Register a new user
    private void registerHandler(Context context) {
        Account newAccount = context.bodyAsClass(Account.class);
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        // Where I validate username and password
        if (username == null || username.isBlank()) {
            context.status(400).result("");  // Username is blank
            return;
        }

        if (password == null || password.length() < 4) {
            context.status(400).result("");  // Password too short
            return;
        }
        // Check if username exists
    Account existingAccount = accountService.getAccountByUsername(username);
    if (existingAccount != null) {
        context.status(400).result("");  // Username already exists returns 400
        return;
    }
    // Create new account
    Account createdAccount = accountService.createAccount(newAccount);
    if (createdAccount != null) {
        context.status(200).json(createdAccount);  // Successfully created account returns 200
    } else {
        context.status(400).result("");  // Failure (e.g., database issue)
    }
        
    }
    // Create new message
    private void createMessageHandler(Context context) {
    Message message = context.bodyAsClass(Message.class);

    // if message text is empty or exceeds 255 characters
    if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
        context.status(400).result("");  // Blank message text returns 400
        return;
    }

    if (message.getMessage_text().length() > 255) {
        context.status(400).result("");  // Message longer than 255 characters returns 400
        return;
    }

    
    Message createdMessage = messageService.createMessage(message);
    if (createdMessage != null) {
        context.status(200).json(createdMessage);  // Succes
    } else {
        context.status(400).result("");  //failure
    }
}

private void loginHandler(Context context) {
    Account loginRequest = context.bodyAsClass(Account.class);  
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    // Perform login by validating the username and password
    Account loggedInUser = accountService.login(username, password);

    if (loggedInUser != null) {
        context.status(200).json(loggedInUser);  
    } else {
        context.status(401).result(""); 
    }
}

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            context.status(200).json(message);  // If message is found
        } else {
            context.status(200).result("");  // If message is not found
        }
    }
    
    //by ID
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage != null) {
            context.status(200).json(deletedMessage);  // Success:deleted
        } else {
            context.status(200).result("");  // Empty body
        }
    }    
    
    // // Delete a message by ID
    // private void deleteMessageHandler(Context context) {
    //     int messageId = Integer.parseInt(context.pathParam("id"));
    //     Message deletedMessage = messageService.deleteMessage(messageId);
    //     if (deletedMessage != null) {
    //         context.status(200).json(deletedMessage);
    //     } else {
    //         context.status(404).result("Message not found.");
    //     }
    // }

    private void updateMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        Message newMessage = context.bodyAsClass(Message.class);
        String newText = newMessage.getMessage_text();
    
        // Validate the message text
        if (newText == null || newText.isBlank()) {
            context.status(400).result("");  // Empty message string
            return;
        }
        
        if (newText.length() > 255) {
            context.status(400).result("");  // Message too long
            return;
        }
         
    Message updatedMessage = messageService.updateMessage(messageId, newText);
    if (updatedMessage != null) {
        context.status(200).json(updatedMessage);  // Success
    } else {
        context.status(400).result("");  // Message not found or update failed
    }
}

    
    private void getMessagesByUserHandler(Context context) {
    int userId = Integer.parseInt(context.pathParam("id"));
    List<Message> messages = messageService.getMessagesByUserId(userId);
    context.status(200).json(messages);  // Return the list of messages (can be empty)
}

}



    // public Javalin startAPI() {
    //     Javalin app = Javalin.create();
    //     app.get("example-endpoint", this::exampleHandler);

