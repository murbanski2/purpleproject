package edu.wctc.distjava.purpleproject.domain;

import java.util.Collection;

/**
 *
 * @author jlombardo
 */
public class UserTypeDecorator {
    private String userName;
    private User user;
    private boolean admin;

    public UserTypeDecorator(User user) {
        this.user = user;
        Collection<Authority> auths = user.getAuthoritiesCollection();

        for (Authority a : auths) {
            if (a.getAuthority().equals("ROLE_ADMIN")) {
                admin = true;
                break;
            }
        }
        userName = user.getUserName();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
}
