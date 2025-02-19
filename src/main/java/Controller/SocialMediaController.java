package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//import org.eclipse.jetty.http.HttpTester.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.SocialMediaService;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    private SocialMediaService service = new SocialMediaService();

    public SocialMediaController() {
        this.service = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.start(8080);
        app.post("/register", this::registration);
        app.post("/login", this::loggingIn);
        app.post("/messages", this::messaging);
        
        app.get("/messages",this::allMessages);
        app.get("/messages/{message_id}", this::getMessageById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registration(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        // SocialMediaService service = new SocialMediaService();
        Account account;
        try {
            account = mapper.readValue(context.body(), Account.class);
            Account created = service.registration(account);
            if (created == null) {
                context.status(400);
            } 
            else {
                try {
                    context.json(mapper.writeValueAsString(created));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonMappingException e) {  
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } 
    }

    private void loggingIn(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        // SocialMediaService service = new SocialMediaService();
        Account account;
        try {
            account = mapper.readValue(context.body(), Account.class);
            Account loggedIn = service.logginIn(account);
            if (loggedIn == null) {
                context.status(401);
            } 
            else {
                try {
                    context.json(mapper.writeValueAsString(loggedIn));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }   catch (JsonMappingException e) {  
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } 
    }

    private void messaging(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        Message message;

        try {
            message = mapper.readValue(context.body(), Message.class);
            Message messageCreated = service.messaging(message);
            if (messageCreated == null) {
                context.status(400);
            }
            else {
                try {
                    context.json(mapper.writeValueAsString(messageCreated));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonMappingException e) {  
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } 


        
    }

    private void allMessages(Context context) {
        List<Message> messages = service.allMessage();
        context.json(messages);
    }

    private void getMessageById(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = service.getMessageById(id);
        if (message == null) { 
            context.status(200);
            context.result("");
        }
        else {
            context.json(message);
        }

    }
}
