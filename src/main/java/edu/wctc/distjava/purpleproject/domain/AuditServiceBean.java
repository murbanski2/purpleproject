/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.distjava.purpleproject.domain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class is a general purpose auditing service that can be used to
 * store certain operations as database entries. It's mainly here to demonstrate
 * transactional operations (see EmployeeEAO.deleteWithAudit)
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AuditServiceBean {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public final void logTransaction(Object id, Class entityClass, String action) {
        String sId = id.toString();
        LogAction la = new LogAction();
        la.setRecId(sId);
        la.setAction(action);
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
