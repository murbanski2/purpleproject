package edu.wctc.distjava.purpleproject.service;

import com.google.common.collect.Lists;
import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.repository.AuthorityRepository;
import edu.wctc.distjava.purpleproject.repository.UserRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.util.LinkedHashSet;
import java.util.Set;

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
@Repository("userService")
@Transactional(readOnly=true)
public class UserService implements IUserService, Serializable {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private AuthorityRepository authRepo;
    
    public UserService() {
    }
    
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delelteAuthorityById(Integer id) {
        authRepo.delete(id);
        authRepo.flush();
    }
    
    public List<Authority> getAuthorities() {
        return authRepo.findAll();
    }
    
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public User saveAndFlush(User entity) {
        return userRepo.saveAndFlush(entity);
    }
    
    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public User save(User entity) {
        return userRepo.saveAndFlush(entity);
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepo.findOne(username);
    }
    
    @Override
    public List<User> findByEnabled(boolean enabled) {
        return userRepo.findByEnabled(enabled);
    }
    
    @Override
    public List<User> findUsersByAuthority(String authority) {
        List<Authority> auths = authRepo.findByAuthority(authority);
        List<String> userNames = new ArrayList<String>(auths.size());
        
        for(Authority a : auths) {
            userNames.add(a.getUsername());
        }
        
        List<User> users = Lists.newArrayList(userRepo.findAll(userNames));
        
        return users;
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public UserRepository getUserRepo() {
        return userRepo;
    }

    @Override
    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    
}
