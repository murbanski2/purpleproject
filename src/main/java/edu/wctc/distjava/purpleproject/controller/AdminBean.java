package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.IUserService;
import edu.wctc.distjava.purpleproject.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author jlombardo
 */
@Named
@Scope("session")
public class AdminBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(AdminBean.class);
    private transient ApplicationContext ctx; // used to get Spring beans 
    private String userName;
    private String selectedType;
    private List<String> memberTypes;
    private List<User> membersFound;
    private User selectedMember;
    private String expireType;
    private List<String> expireTypes;
    private List<AuctionItem> itemsFound;
    
    public AdminBean() {
        memberTypes = new ArrayList<String>();
        memberTypes.add("Enabled");
        memberTypes.add("Disabled");
        memberTypes.add("Administrator");
        
        expireTypes = new ArrayList<String>();
        expireTypes.add("Today");
        expireTypes.add("This Week");
        expireTypes.add("This Month");
        
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance()); 
    }
    
    public String doItemByExpireTypeSearch() {
        IAuctionItemService aucSrv = 
                    (IAuctionItemService) ctx.getBean("auctionItemService");
        
        if(expireType.equals("Today")) {
            itemsFound = aucSrv.findByEndDatesToday();
        } else if(expireType.equals("This Week")) {
            itemsFound = aucSrv.findByEndDatesThisWeek();
        } else {
            itemsFound = aucSrv.findByEndDatesThisMonth();
        }
        
        return null; // destination = same page
    }
    
    public String doMemberSearch() {
        // We could inject this as a class property, but that would
        // use memory inefficiently. This way we only have a local variable
        IUserService userSrv = 
                    (IUserService) ctx.getBean("userService");
        membersFound = new ArrayList<User>();
        
        if(userName != null && userName.length() > 0) {
            membersFound.add(userSrv.findByUsername(userName));
            
        } else {
            if(selectedType.equals("Enabled")) {
                membersFound = userSrv.findByEnabled(true);
            } else if(selectedType.equals("Disabled")) {
                membersFound = userSrv.findByEnabled(false);
            } else {
                membersFound = userSrv.findUsersByAuthority("ROLE_ADMIN");
            }

        }
        
        return null; // destination = same page
    }
    
    public void handleMemberUpdate(RowEditEvent event) {
        User member = (User) event.getObject(); 
        IUserService userSrv = 
                    (IUserService) ctx.getBean("userService");
        try {
            userSrv.save(member);
            FacesUtils.addSuccessMessage("Member info updated.");
        } catch(DataAccessException dae) {
            FacesUtils
                    .addErrorMessage(
                    "Sorry, the member info could not be updated due to: " 
                    + dae.getMessage());
        }
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

    public String getExpireType() {
        return expireType;
    }

    public void setExpireType(String expireType) {
        this.expireType = expireType;
    }

    public List<String> getExpireTypes() {
        return expireTypes;
    }

    public void setExpireTypes(List<String> expireTypes) {
        this.expireTypes = expireTypes;
    }

    public List<AuctionItem> getItemsFound() {
        return itemsFound;
    }

    public void setItemsFound(List<AuctionItem> itemsFound) {
        this.itemsFound = itemsFound;
    }

    
}
