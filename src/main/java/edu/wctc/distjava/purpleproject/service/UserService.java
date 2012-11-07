package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.repository.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a Spring-managed, transactional service class that 
 * decouples the DAOs from the view layer. By default all methods are
 * read only transactions for optimal performance. This behavior may be 
 * overridden on a method by method basis by using the following annotation:
 * '@Transactional(readOnlly=false)'
 * 
 * @author      Jim Lombardo
 * @version     1.01
 */
@Repository
@Transactional(readOnly=true)
public class UserService implements IUserService {
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private UserRepository userRepo;
    
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public User saveAndFlush(User entity) {
        return userRepo.saveAndFlush(entity);
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepo.findOne(username);
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    
}
