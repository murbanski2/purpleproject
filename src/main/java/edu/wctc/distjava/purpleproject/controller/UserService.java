package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.domain.UserEAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class is a JSF Managed Bean. However, it looks different from a 
 * normal JSF bean because it does not use the @ManagedBean annotation.
 * Instead it uses the @Named annotation, which is a feature of a new JEE
 * technology called CDI. Think of CDI as having both ManagedBean capabilities
 * plus others, including dependency injection.
 * 
 * Additionally, I chose to name this bean a "Service" bean because it
 * fulfills that role. This is a personal choice, but an appropriate one. YOu
 * could also argue this bean is a Controller. Also I've made this a
 * ConversationScoped bean (CDI scope, not JSF scope). This is slightly 
 * heavier in resource use than a RequestScoped bean, but substantially lighter
 * than a SessionScoped bean. It's perfect for this demo which takes the user
 * through several related views to accomplish a goal. NOTE: when using
 * ConversatiobScoped you MUST inject a Conversation object and use
 * conversation.begin() and conversation.end().
 * 
 * Finally, notice the @Iject annotation. This is one way to inject an 
 * Enterprise Java Bean (EJB). However, it requires a java application server
 * that is a full-stack server like Glassfish. This won't work on servers 
 * that are just servlet containers or those that don't support EJBs. You can 
 * also use @Inject for POJOs. (Only CDI allows injection of POJOs.) Were we
 * to use Spring, we would have all of these capabilities without needing a
 * JEE 6 certified server -- one of the big advantages of using Spring. And
 * yet if youd do use Spring on a certified server, Spring is smart of enough
 * to defer to the server for resource management.
 * 
 * @author     Jim Lombardo
 * @version    1.02
 */
@Named(value = "userSrv")
@ConversationScoped
public class UserService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserEAO userEAO;
    
    @Inject 
    private Conversation  conversation;
        
    @PostConstruct
    public void init(){
         // No need to check here because we're in init
         getConversation().begin();
    }
    
    public void endConversation(ActionEvent e) {
        // Check first or you could get an exception if, e.g.,
        // the conversation has timed out
         if(!getConversation().isTransient()) {
             getConversation().end();
         }
    }
    /**
     * Creates a new instance of EmployeeService
     */
    public UserService() {
    }
    
    public void setAuthority(String value) {
        getUserEAO().setAuthority(value);
    }
    
    public void setUsername(String value) {
        getUserEAO().setUsername(value);
    }
    
    public void setPassword(String value) {
        getUserEAO().setPassword(value);
    }
    
    // Post first part of 2-part conversation
    public String processAuthorityRequest() {
        return "index5";
    }
    
    // Post 2nd part of 2-part conversation
    public String saveNew() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        User user = new User();
        user.setUsername(getUserEAO().getUsername());
        user.setPassword(getUserEAO().getPassword());
        user.setEnabled(true);
        
        if(!getUserEAO().isUsernameInUse(getUserEAO().getUsername())) {
            getUserEAO().addNewUser(user);
            context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", 
                    "User saved successfully!"));
            
        } else {
            context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Duplicate username", 
                    "User not saved. username already in use. Please choose another."));
        }
        
        return null; // back to same page
    }

    public String getAuthority() {
        return "ROLE_MEMBER";
    }

    public String getUsername() {
        return "";
    }

    public String getPassword() {
        return "";
    }

    public UserEAO getUserEAO() {
        return userEAO;
    }

    public Conversation getConversation() {
        return conversation;
    }


 }
