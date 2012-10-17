package edu.wctc.distjava.purpleproject.domain;

import java.util.List;
import javax.ejb.EJB;
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
 * One more thing. JEE v6+ allows the creation of EJBs without first
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
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EmployeeEAO extends AbstractEAO<Employee> {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em; // never refer to this!!! (see below)
    
    @EJB
    AuditServiceBean audit;

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
     * You can see this work by uncommenting one of the lines below
     * with the exception, which will cause an artificial failure. Try
     * removing an Employee and then notice it remains in the database 
     * because Glassfish (via JTA in the EJB container) rolled back the
     * changes automatically. If you uncomment only the second exception,
     * both operations (remove and logTrancaction) will be rolled back.
     * 
     * @param employee - the entity to be deleted.
     */  
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteWithAudit(Integer id) throws Exception {
        Employee e = getEntityManager().find(Employee.class, id);
        getEntityManager().remove(e);
//        if(true) throw new IllegalArgumentException("Artificial to demo rollback");
        audit.logTransaction(id, Employee.class, "Employee deleted.");
//        if(true) throw new IllegalArgumentException("Artificial to demo rollback");        
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
