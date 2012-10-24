package edu.wctc.distjava.purpleproject.domain;

import java.util.List;
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
 *
 * @author jlombardo
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@ApplicationException(rollback = true)
public class UserEAO extends AbstractEAO<User> {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)
    
    @Resource
    private SessionContext ctx;

    /** 
     * Default constructor must be declared this way when extending
     * AbstractEAO
     */
    public UserEAO() {
        super(User.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addNewUser(User user, String authority) {
        getEntityManager().persist(user);
        Authority auth = new Authority();
        auth.setUsername(user);
        auth.setAuthority(authority);
        getEntityManager().persist(auth);
    }
    
    public boolean isUsernameInUse(String username) {
        boolean isUsed = true;
        
        Query q = getEntityManager().createNamedQuery("Users.findByUsername");
        List results = q.getResultList();
        if(results.isEmpty()) isUsed = false;
        
        return isUsed;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


}
