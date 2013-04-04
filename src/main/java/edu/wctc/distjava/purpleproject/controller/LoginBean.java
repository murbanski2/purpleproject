package edu.wctc.distjava.purpleproject.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import edu.wctc.distjava.purpleproject.util.FacesUtils;
import java.util.Collection;
import javax.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jlombardo
 */
@Named
@Scope("request")
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
     * Note that @SuppressWarnings is only used by a source code analyzer
     * that I use caled "FindBugs". You don't need this unless you do to.
     */
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(LoginBean.class);

    private String userName = "";
    private String password = "";
    @Resource(name = "authenticationManager")
    private AuthenticationManager am;
    
    public LoginBean() {
    }

    /**
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public String doLogin() throws IOException, ServletException {
        String destinationPage = "/faces/login.xhtml";
        
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                .getRequestDispatcher("/j_spring_security_check?j_username=" + userName
                                + "&j_password=" + password);
        dispatcher.forward((ServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();

        // It's OK to return null here because Faces is just going to exit.
        return null;
    }
    
    /**
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param username
     */
    public void setUserName(final String userName) {
        this.userName = userName;
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

    public AuthenticationManager getAm() {
        return am;
    }

    public void setAm(AuthenticationManager am) {
        this.am = am;
    }
    
}
