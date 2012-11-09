package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IUserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * This Spring-managed JSF/CDI bean handles the member registration process 
 * from provision of credentials, to acceptance of terms of service, to the
 * sending of an email for verification purposes, and finally to acceptance
 * and confirmation of member registration.
 * <P>
 * Because this process spans several pages, the class uses CDI 
 * ConverstaionScope to retain data from page to page.
 * <P>
 * IMPORTANT NOTES: Spring does not support ConversationScoped (from CDI),
 * therefore, we are using Spring's session scope.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Named
@RequestScoped
public class RegistrationBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(RegistrationBean.class);
    private transient ApplicationContext ctx; // used to get Spring beans

    private String username = "";
    private String password = "";
    
    public RegistrationBean() {
        // DO NOT initialize ApplicationContext here. Spring creates
        // proxy objects so you won't have direct access to this constructor.
    }
    
    /**
     * Handles POST of member credentials and redirects to 
     * agreement page.
     * 
     * @return the destination page
     */
    public String doAgreement() {
        return "registrationAgreement";
    }
    
    /**
     * Should the visitor decline the terms of service on the agreement
     * page, this ends the conversation and redirects to the home page.
     * 
     * @return the destination page
     */
    public String doDecline() {
        return "index"; // back to home page
    }
    
    /**
     * Handles part one of the registration process, which is to record
     * the user credentials in the database, including a salted hash of the 
     * password, but disable the account until the email verification process 
     * is completed.
     * <P>
     * Note that the username is the primary key in the db and, therefore, must 
     * be unique. A query is performed to check whether the requested username 
     * is already being used, and if so, redirect back with a message 
     * stating this.
     * 
     * @return the destination page
     */
    public String doRegistration() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());
        
        // Retrieve Spring bean
        IUserService userSrv = (IUserService)ctx.getBean("userService");
        // Needed for JSF Messages
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Determine if user already exists
        User curUser = userSrv.findByUsername(username);
        if(curUser != null) {
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                "User Already Exists", "Sorry, that user email is already in use."));
            
            return "registrationForm"; // go back to form to try again
        }
        
        // If new user, prepare salted hash for password
        String hash = sha512(password, username);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(hash);
        user.setEnabled(false); // don't want enabled until email verified!
        
        List<Authority> auths = new ArrayList<Authority>();
        Authority auth = new Authority();
        auth.setAuthority("ROLE_MEMBER");
        auths.add(auth);
        user.setAuthoritiesCollection(auths);
        auth.setUsername(user);
        
        //@TODO Needs an exception handler!!!!
        user = userSrv.saveAndFlush(user);
        
        // Now send the verification email using Spring's help
        // @see http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mail.html#mail-introduction
        ISimpleMailSender emailSender = 
                (ISimpleMailSender)ctx.getBean("emailSender");
        
        try {
            emailSender.sendUserEmailVerification(user);
        } catch(MailException ex) {
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Email Failure", "Sorry, the verification email could not be "
                    + "sent. Please notify the webmaster at "
                    + "bitbay.webmaster@gmail.com and we'll complete the "
                    + "process for you. Thanks for your patience."));
        }
        
        return "registrationConfirmed";
    }
    
    /*
     * Creates a salted SHA-512 hash composed of password (pwd) and 
     * salt (username).
     */
    private String sha512(String pwd, String salt) {
        ShaPasswordEncoder pe = new ShaPasswordEncoder(512);
        pe.setIterations(1024);
        String hash = pe.encodePassword(pwd, salt);

        return hash;
    }

    /**
     * @return
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password
     */
    public void setPassword(final String password) {
        this.password = password;
    }    

    
}
