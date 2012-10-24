package edu.wctc.distjava.purpleproject.domain;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * This class is a Stateless EJB. We need an EJB because we want the methods
 * to be transactional and the EJB Container provides Container Managed
 * Transactions automatically (less code for us to write!). Alternatively
 * we could use Spring and let Spring manage the transactions, or we could
 * write them ourselves (uugh!).
 * <P>
 * Why a "Stateless" EJB? Because this class simply provides data to other
 * objects. No need to store anything long-term here. And stateless EJBs 
 * are much more performant and memory efficient than "Statfull" EJBs.
 * <P>
 * Additionally. JEE v6+ allows the creation of EJBs without first
 * declaring local or remote (or both) interfaces (see the JEE tutorial). This
 * is a convenience for us programmers, but it also means that this EJB is
 * a "Local" EJB and we cannot make this remotely accessible, which has benefits 
 * in certain situations. For example, imagine a service that runs on a server 
 * other than the one where this application is running. With a Remote EJB we
 * could access that service from this application which is running on a
 * different server. We could also cluster a group of servers to run a bunch
 * of remote EJBs, allowing for great scalability. But none of that can 
 * happen when we use a local EJB. Declaring the way we have makes this the
 * only option.
 * <P>
 * You can test transaction rollback by uncommenting code as instructed
 * in the AuditServiceBean.
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
public class EmployeeEAO extends AbstractEAO<Employee> {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)
    
    @Resource
    private SessionContext ctx;
    
    @EJB
    AuditServiceBean audit;

    /** 
     * Default constructor must be declared this way when extending
     * AbstractEAO
     */
    public EmployeeEAO() {
        super(Employee.class);
    }

    /**
     * This method delegates is a 'Projection Query" which
     * means that you get back a List of Object[] instead of an entity. This
     * is a useful optimization technique because you get back only the raw
     * data you want instead of entity objects. Also good for joins where
     * you don't want to deal with Lazy Loading issues.
     * 
     * @return - Object[] for each record 
     */
    public List getNamesHireDate() {
        return getEntityManager().createQuery(
                            "SELECT e.id, e.lastname, e.firstname, e.hiredate " +
                            "FROM Employee e")
                    .getResultList();
    }
    
    // Same as above but self-documenting. The choice of style is
    // a matter of personal preference.
//    public List<Object[]> getNamesHireDate() {
//        TypedQuery<Object[]> query = em.createQuery(
//                            "SELECT e.id, e.lastname, e.firstname, e.hiredate " +
//                            "FROM Employee e", Object[].class);
//        return query.getResultList();
//    }
    
    /**
     * This method deletes an Employee. But we've also added code to
     * demonstrate a two-step transaction where if either step fails the
     * entire transaction fails. 
     * <P>
     * You can see the rollback work by uncommenting code as described in the
     * AudieServiceBean.logTransaction method, or by uncommenting the
     * code below.
     * 
     * @param employee - the entity to be deleted.
     */  
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteWithAudit(Integer id) throws Exception {
        Employee e = getEntityManager().find(Employee.class, id);
        getEntityManager().remove(e);
        
        getEntityManager().flush();
        
        // To trigger a rollback if your code does something wrong but
        // doesn't throw an exception, simply do the following...
//        if(true) { // change true to a real test for your code!
//            ctx.setRollbackOnly();
//        }
       
        audit.logTransaction(id, Employee.class);
    }

    /**
     * Always use this in your code -- never reference the 'em' property 
     * because JPA EntityManagers are loaded on demand.
     * 
     * @return an on demand object
     */
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
}
