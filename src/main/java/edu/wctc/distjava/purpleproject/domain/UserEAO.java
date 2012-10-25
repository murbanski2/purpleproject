package edu.wctc.distjava.purpleproject.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This is a Stateful EJB. In this demo I'm using it to store the state
 * of the long running conversation that takes place during the multiple
 * views to gather new user information. This state could just as easily have
 * been saved in the JSF bean, but here the advantage is that the state is
 * closer to where it is being used.
 * 
 * @author      Jim Lombardo
 * @version     1.02
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@ApplicationException(rollback = true)
public class UserEAO extends AbstractEAO<User> {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)
    
    @Resource
    private SessionContext ctx;
    
    private String authority;
    private String username;
    private String password;

    /** 
     * Default constructor must be declared this way when extending
     * AbstractEAO
     */
    public UserEAO() {
        super(User.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addNewUser(User user) {
        Authority auth = new Authority();
        auth.setUsername(user);
        auth.setAuthority(authority);
        auth.setAuthoritiesId(null);
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(auth);
        user.setAuthoritiesCollection(authorities);
        getEntityManager().merge(user);
    }
    
    public boolean isUsernameInUse(String username) {
        boolean isUsed = true;
        
        Query q = getEntityManager().createNamedQuery("User.findByUsername");
        q.setParameter("username", username);
        Collection users = q.getResultList();
        if(users.isEmpty()) isUsed = false;
        
        return isUsed;
    }
    
    @PreDestroy
    private void destroy() {
        System.out.println("*** UserEAO EJB Destroyed!");
        authority = null;
        username = null;
        password = null;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
