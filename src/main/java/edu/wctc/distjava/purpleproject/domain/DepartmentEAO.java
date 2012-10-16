package edu.wctc.distjava.purpleproject.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jlombardo
 */
@Stateless
public class DepartmentEAO extends AbstractEAO<Department> {
    @PersistenceContext(unitName = "purpleproject_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartmentEAO() {
        super(Department.class);
    }

}
