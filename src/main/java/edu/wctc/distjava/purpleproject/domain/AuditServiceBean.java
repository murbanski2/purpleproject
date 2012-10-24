/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.distjava.purpleproject.domain;

import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class is a general purpose auditing service that can be used to
 * store certain operations as database entries. It's mainly here to demonstrate
 * participation in an existing transactional (see EmployeeEAO.deleteWithAudit).
 * <P>
 * You can test transaction rollback by uncommenting code as instructed
 * below.
 * <P>
 * The "NOT_SUPPPORTED TransactionAttribute simply means do not use
 * transactions by default. This is good for read-only operations as it
 * optimizes processing. However, for write operations a separate 
 * TransactionAttribute annotation is needed for those methods, with the
 * attribute type set to REQUIRED.
 * <P>
 * The ApplicationException annotation (true) is used to allow application-
 * level exceptions to trigger a rollback. Normally, only RuntimeExceptions
 * will trigger a CMT rollback.
 * 
 * @author  Jim Lombardo
 * @version 1.01
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@ApplicationException(rollback = true)
public class AuditServiceBean {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public final void logTransaction(Object id, Class entityClass) {
        String sId = id.toString();
        
        // Use this to test rollback
//        LogAction la = null;
        
        // Comment this out to test rollback
        LogAction la = new LogAction();
        la.setRecId(sId);
        la.setAction(entityClass.getName() + " deleted");
        getEntityManager().persist(la);
    }
    
    /**
     * Always use this in your code -- never reference the 'em' property 
     * because JPA EntityManagers are loaded on demand.
     * 
     * @return an on demand object
     */
    public EntityManager getEntityManager() {
        return em;
    }
}
