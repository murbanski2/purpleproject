package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IUserService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author jlombardo
 */
@Named
@RequestScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(LoginBean.class);
    private transient ApplicationContext ctx;

    private String username = "";
    private String password = "";
    private boolean loggedIn = false;
    
    public LoginBean() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());
    }

    /**
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public String doLogin() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                .getRequestDispatcher("/j_spring_security_check?j_username=" + username
                                + "&j_password=" + password);
        dispatcher.forward((ServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        // It's OK to return null here because Faces is just going to exit.
        return null;
    }
    
    public String doRegistration() {
        IUserService userSrv = (IUserService)ctx.getBean("userService");
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Determine if user already exists
        User curUser = userSrv.findByUsername(username);
        if(curUser != null) {
            context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                "User Already Exists", "Sorry, that user email is already in use."));
            return null; // go back to same page
        }
        
        // If new user, prepare salted hash for password
        String salt = username; // username field in db
        ShaPasswordEncoder pe = new ShaPasswordEncoder(512);
        pe.setIterations(1024);
        String hash = pe.encodePassword(password, salt);
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(hash);
        user.setEnabled(true);
        
        List<Authority> auths = new ArrayList<Authority>();
        Authority auth = new Authority();
        auth.setAuthority("ROLL_MEMBER");
        auths.add(auth);
        user.setAuthoritiesCollection(auths);
        auth.setUsername(user);
        userSrv.saveAndFlush(user);
        
        return "registrationConfirmed";
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

    /**
     * @return
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    /**
     * @param loggedIn
     */
    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    
    
}
