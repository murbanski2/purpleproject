package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.repository.UserRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface IUserService {

    @Modifying  
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    User saveAndFlush(User entity);

    @Modifying   
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    User save(User entity);
    
    User findByUsername(String username);
    
    EntityManager getEm();
    
    void setEm(EntityManager em);
    
    UserRepository getUserRepo();
    
    void setUserRepo(UserRepository userRepo);
    
    List<User> findUsersByAuthority(String authority);
    
    List<User> findByEnabled(boolean enabled);
}
