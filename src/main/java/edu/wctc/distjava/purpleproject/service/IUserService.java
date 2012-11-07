package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jlombardo
 */
public interface IUserService {

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    User saveAndFlush(User entity);

    User findByUsername(String username);
}
