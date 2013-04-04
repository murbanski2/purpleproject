package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.PopularItemDto;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.IUserService;
import edu.wctc.distjava.purpleproject.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.web.jsf.FacesContextUtils;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.UserTypeDecorator;
import java.util.Collection;

/**
 *
 * @author jlombardo
 */
@Named
@Scope("session")
public class AdminBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /*
     * Note that @SuppressWarnings is only used by a source code analyzer
     * that I use caled "FindBugs". You don't need this unless you do to.
     */
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(AdminBean.class);
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient ApplicationContext ctx; // used to get Spring beans 
    private String userNameForSearch;
    private String selectedType;
    private List<String> memberTypes;
    private List<UserTypeDecorator> membersFound;
    private UserTypeDecorator selectedMember;
    private String expireType;
    private List<String> expireTypes;
    private String popularType;
    private List<String> popularTypes;
    private List<AuctionItem> itemsFound;
    private List<PopularItemDto> popularItemsFound;

    public AdminBean() {
        memberTypes = new ArrayList<String>();
        memberTypes.add("Enabled");
        memberTypes.add("Disabled");
        memberTypes.add("Administrator");

        expireTypes = new ArrayList<String>();
        expireTypes.add("Today");
        expireTypes.add("This Week");
        expireTypes.add("This Month");

        popularTypes = new ArrayList<String>();
        popularTypes.add("Curently Active");
        popularTypes.add("All Time");

        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());

    }

    public void doItemByPopularitySearch(ActionEvent event) {
        final boolean ACTIVE = false;
        final boolean ALL_TIME = true;
        IAuctionItemService aucSrv =
                (IAuctionItemService) ctx.getBean("auctionItemService");

        if (popularType.equals("Currently Active")) {
            popularItemsFound = aucSrv.findByMostPopular(ACTIVE);
        } else {
            popularItemsFound = aucSrv.findByMostPopular(ALL_TIME);
        }
    }

    public void doItemByExpireTypeSearch(ActionEvent event) {
        IAuctionItemService aucSrv =
                (IAuctionItemService) ctx.getBean("auctionItemService");

        if (expireType.equals("Today")) {
            itemsFound = aucSrv.findByEndDatesToday();
        } else if (expireType.equals("This Week")) {
            itemsFound = aucSrv.findByEndDatesThisWeek();
        } else {
            itemsFound = aucSrv.findByEndDatesThisMonth();
        }
    }

    public void doMemberSearch(ActionEvent event) {
        // We could inject this as a class property, but that would
        // use memory inefficiently. This way we only have a local variable
        IUserService userSrv =
                (IUserService) ctx.getBean("userService");
        List<User> usersFound = null;
        membersFound = new ArrayList<UserTypeDecorator>();

        if (userNameForSearch != null && userNameForSearch.length() > 0) {
            User member = userSrv.findByUsername(userNameForSearch);
            UserTypeDecorator user = new UserTypeDecorator(member);
            membersFound.add(user);

        } else {
            if (selectedType.equals("Enabled")) {
                usersFound = userSrv.findByEnabled(true);
                copyToMembersFound(usersFound);
                
            } else if (selectedType.equals("Disabled")) {
                usersFound = userSrv.findByEnabled(false);
                copyToMembersFound(usersFound);
                
           } else {
                usersFound = userSrv.findUsersByAuthority("ROLE_ADMIN");
                copyToMembersFound(usersFound);
            }
        }
    }
    
    private void copyToMembersFound(List<User> usersFound) {
        for(User u : usersFound) {
            membersFound.add(new UserTypeDecorator(u));
        }
    }

    public void handleMemberUpdate(RowEditEvent event) {
        UserTypeDecorator userDecorator = (UserTypeDecorator) event.getObject();

        removeOrAddAdminAuthority(userDecorator);

        IUserService userSrv =
                (IUserService) ctx.getBean("userService");
        try {
            User user = userSrv.saveAndFlush(userDecorator.getUser());
            FacesUtils.addSuccessMessage("Member info updated.");
        } catch (DataAccessException dae) {
            FacesUtils
                    .addErrorMessage(
                    "Sorry, the member info could not be updated due to: "
                    + dae.getMessage());
        }
    }

    private void removeOrAddAdminAuthority(UserTypeDecorator userDecorator) {
        User member = userDecorator.getUser();
        Collection<Authority> auths = member.getAuthoritiesCollection();
        boolean wasAdmin = false;

        for (Authority a : auths) {
            if (a.getAuthority().equals("ROLE_ADMIN")) {
                wasAdmin = true;
                break;
            }
        }

        if (userDecorator.isAdmin() && !wasAdmin) {
            // add admin role
            Authority adminAuth = new Authority();
            adminAuth.setAuthority("ROLE_ADMIN");
            adminAuth.setUsername(member.getUserName());
            auths.add(adminAuth);

        } else if (!userDecorator.isAdmin() && wasAdmin) {
            // remove admin role
            for (Authority a : auths) {
                if (a.getAuthority().equals("ROLE_ADMIN")) {
                    auths.remove(a);
                    break;
                }
            }
        }
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getUserNameForSearch() {
        return userNameForSearch;
    }

    public void setUserNameForSearch(String userNameForSearch) {
        this.userNameForSearch = userNameForSearch;
    }

    public List<String> getMemberTypes() {
        return memberTypes;
    }

    public void setMemberTypes(List<String> memberTypes) {
        this.memberTypes = memberTypes;
    }

    public UserTypeDecorator getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(UserTypeDecorator selectedMember) {
        this.selectedMember = selectedMember;
    }

    public List<UserTypeDecorator> getMembersFound() {
        return membersFound;
    }

    public void setMembersFound(List<UserTypeDecorator> membersFound) {
        this.membersFound = membersFound;
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

    public String getPopularType() {
        return popularType;
    }

    public void setPopularType(String popularType) {
        this.popularType = popularType;
    }

    public List<String> getPopularTypes() {
        return popularTypes;
    }

    public void setPopularTypes(List<String> popularTypes) {
        this.popularTypes = popularTypes;
    }

    public List<PopularItemDto> getPopularItemsFound() {
        return popularItemsFound;
    }

    public void setPopularItemsFound(List<PopularItemDto> popularItemsFound) {
        this.popularItemsFound = popularItemsFound;
    }

}
