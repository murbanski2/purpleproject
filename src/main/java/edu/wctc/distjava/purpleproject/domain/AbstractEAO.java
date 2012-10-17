package edu.wctc.distjava.purpleproject.domain;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * This class is the general contract for all EAO (Entity Access Objects --
 * essential a type of DAO) implementations. Put only common methods and
 * properties in this class.
 * <P>
 * Note the use of generics which makes this useable with many different
 * entity classes.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
public abstract class AbstractEAO<T> {
    private Class<T> entityClass;

    public AbstractEAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract EntityManager getEntityManager();

    public final void create(T entity) {
        getEntityManager().persist(entity);
    }

    public final void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public final void removeById(Object id) {
        T entity = find(id);
        remove(entity);
    }

    public final void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public final T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public final List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public final List<T> findRange(int startRecNo, int endRecNo) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(endRecNo - startRecNo);
        q.setFirstResult(startRecNo);
        return q.getResultList();
    }

    public final int getCount() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    

}
