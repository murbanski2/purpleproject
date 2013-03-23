package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.IUserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author jlombardo
 */
@Named
@Scope("session")
public class AdminMemberBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(AdminMemberBean.class);
    private transient ApplicationContext ctx; // used to get Spring beans   
    private String userName;
    private String selectedType;
    private List<String> memberTypes;
    private List<User> membersFound;
    private User selectedMember;
    
    public AdminMemberBean() {
        memberTypes = new ArrayList<String>();
        memberTypes.add("Active");
        memberTypes.add("Inactive");
        memberTypes.add("Administrator");
    }
    
    public String doMemberSearch() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());          
        IUserService userSrv = 
                    (IUserService) ctx.getBean("userService");
        membersFound = new ArrayList<User>();
        
        if(userName != null && userName.length() > 0) {
            membersFound.add(userSrv.findByUsername(userName));
            
        } else {
            if(selectedType.equals("Active")) {
                membersFound = userSrv.findByEnabled(true);
            } else if(selectedType.equals("Inactive")) {
                membersFound = userSrv.findByEnabled(false);
            } else {
                
            }

        }
        
        return null;
    }
    
    public void handleMemberSelect() {
        
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public List<String> getMemberTypes() {
        return memberTypes;
    }

    public void setMemberTypes(List<String> memberTypes) {
        this.memberTypes = memberTypes;
    }

    public List<User> getMembersFound() {
        return membersFound;
    }

    public void setMembersFound(List<User> membersFound) {
        this.membersFound = membersFound;
    }

    public User getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(User selectedMember) {
        this.selectedMember = selectedMember;
    }

    
}
