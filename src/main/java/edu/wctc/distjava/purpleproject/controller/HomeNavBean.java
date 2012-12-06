package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.AuctionItemDto;
import edu.wctc.distjava.purpleproject.domain.Bid;
import edu.wctc.distjava.purpleproject.domain.MemberSearch;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.jsf.FacesContextUtils;
import org.springframework.web.util.WebUtils;

/**
 * The is a Spring-managed JSF bean and the main entry point for the
 * middleware application.
 * 
 * @author     Jim Lombardo
 * @version    1.07
 */
@Named("homeNavBean")
@Scope("session")
public class HomeNavBean implements Serializable {
    private static final long serialVersionUID = 6L;
    private final Logger LOG = LoggerFactory.getLogger(HomeNavBean.class);
    private static final int MAX_RECORDS = 200;
    private transient ApplicationContext ctx; // used to get Spring beans    
    
    private String noImpMsg = "Not yet Implemented";
    private List<AuctionItemDto> auctionItemsFound;
    public AuctionItemDto selectedAuctionItemDto;
    private List<MemberSearch> recentSearches;
    private String selectedCategory;
    private String searchPhrase;

    public HomeNavBean() {
    }
    
    public void placeBid() {
        
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());          
        FacesContext context = FacesContext.getCurrentInstance();
//        
//        // No UserDetails instsance if anonymous
//        String username = SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal().toString();
//
//        if(username.contains("anonymous")) {
//            context.addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_WARN,
//                    "Registration Required", "Sorry, you must be a registered user to place bids."));
//            return "foundItemsList";
//        }
        
        Bid bid = new Bid();
        bid.setItemId(selectedAuctionItemDto.getItemId());
        
        UserDetails userDetails = (UserDetails)SecurityContextHolder
                .getContext().getAuthentication().getPrincipal(); 
        String username = userDetails.getUsername();

        bid.setBidderId(username);
        bid.setAmount(new BigDecimal(selectedAuctionItemDto.getPlacedBid()));
        IAuctionItemService auctionSrv = 
                    (IAuctionItemService) ctx.getBean("auctionItemService");          
        auctionSrv.saveBid(bid);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        BigDecimal highestBid = 
                auctionSrv.findHighestBidForItem(selectedAuctionItemDto.getItemId());
        double hBid = highestBid == null ? 0 : highestBid.doubleValue();
        selectedAuctionItemDto.setHighBid(nf.format(hBid));
        BigDecimal v1 = new BigDecimal(""+hBid);
        BigDecimal v2 = new BigDecimal(".10");
        v1 = v1.add(v2);
        selectedAuctionItemDto.setMinBid(v1.doubleValue());
        selectedAuctionItemDto.setPlacedBid(v1.doubleValue());
        Number count = auctionSrv.findBidCountForItem(selectedAuctionItemDto.getItemId());
        selectedAuctionItemDto.setBidCount(count.toString());
       
//       return "/faces/member/itemDetails.xhtml?faces-redirect=true";
    }
    
    public void handleItemSelect(SelectEvent e) {
        // auction item has been selected and saved in the
        // selectedAutionItemDto property. Now navigate to details page
        ConfigurableNavigationHandler navigationHandler = 
            (ConfigurableNavigationHandler) FacesContext
            .getCurrentInstance().getApplication().getNavigationHandler();

        navigationHandler.performNavigation("itemDetails.xhtml?faces-redirect=true");
    }
    
    public String redoMemberSearch(String phrase) {
        selectedCategory = null;
        searchPhrase = phrase;
        return doItemSearch();
    }
    
    /**
     * There are two search result pages, one for authenticated users and
     * one for guests. The guest page has a plea for joining, a button and
     * a chart of donations. The authenticated version does not. Therefore,
     * this method checks if user is authenticated and then sends the user
     * to the correct page based on the result. Authenticated users go to the
     * result page under the member folder.
     * 
     * @return destination page
     */
    public String doItemSearch() {
        String authenticatedDestination = "/member/foundItemsList";
        String anonymousDestination = "/foundItemsList";
        
        // No UserDetails instsance if anonymous
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();

        if(username.contains("anonymous")) {
            findItems(null);
            return anonymousDestination;
            
        } else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal(); 
            username = userDetails.getUsername();
            findItems(username);
            return anonymousDestination;
        }
    }
    
    public String showPopularByType(String key) {
        return null;
    }

    public void logout(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        ResourceBundle bundle =
                ResourceBundle.getBundle("messages",
                    		context.getViewRoot().getLocale());
        String contextRoot = bundle.getString("server.context.root");
        try {
            extContext.redirect("/" 
                    + contextRoot + "/j_spring_security_logout");
        } catch (IOException ex) {
            LOG.debug(HomeNavBean.class.getName() + ": " + ex.getMessage());
        }
    }

    public String getNoImpMsg() {
        return noImpMsg;
    }

    public void handleNotImplemented(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN,
                this.noImpMsg, "Coming soon..."));
    }
    
    public void killRregistrationBean(ActionEvent e) {
        // Now remove RegistrationBean from HttpSession
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        extContext.getRequest();
        WebUtils.setSessionAttribute(
                (HttpServletRequest)extContext.getRequest(), 
                "registrationBean", null);

    }

    public String getJavaVersion() {
        return "Java: " + System.getProperty("java.version");
    }

    public String getOsInfo() {
        return "O/S: " + System.getProperty("os.name") + " " 
                + System.getProperty("os.version");
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public List<AuctionItemDto> getAuctionItemsFound() {
        return auctionItemsFound;
    }

    public void setAuctionItemsFound(List<AuctionItemDto> auctionItemsFound) {
        this.auctionItemsFound = auctionItemsFound;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public List<MemberSearch> getRecentSearches() {
        if(recentSearches == null) {
            ctx = FacesContextUtils.getWebApplicationContext(
                    FacesContext.getCurrentInstance());      
            UserDetails userDetails = (UserDetails)SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal(); 
            String username = userDetails.getUsername();
            FacesContext context = FacesContext.getCurrentInstance();
            IAuctionItemService auctionSrv = 
                        (IAuctionItemService) ctx.getBean("auctionItemService");          
            try {
                recentSearches = auctionSrv.findRecentSearchesByUser(username);
            } catch(DataAccessException dae) { 
                LOG.error("Recent member search query failed due to: " 
                        + dae.getMessage());
            }        
        }
        return recentSearches;
    }
    
    public void updateItemById() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());          
        FacesContext context = FacesContext.getCurrentInstance();
        IAuctionItemService auctionSrv = 
                    (IAuctionItemService) ctx.getBean("auctionItemService");  
        
        Integer itemId = selectedAuctionItemDto.getItemId();
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        BigDecimal highestBid = 
                    auctionSrv.findHighestBidForItem(itemId);
        double bid = highestBid == null ? 0 : highestBid.doubleValue();
        selectedAuctionItemDto.setHighBid(nf.format(bid));
        BigDecimal v1 = new BigDecimal(""+bid);
        BigDecimal v2 = new BigDecimal(".10");
        v1 = v1.add(v2);
        selectedAuctionItemDto.setMinBid(v1.doubleValue());
        Number count = auctionSrv.findBidCountForItem(itemId);
        selectedAuctionItemDto.setBidCount(count.toString());
    }

    public void findItems(String userId) {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());          
        FacesContext context = FacesContext.getCurrentInstance();
        IAuctionItemService auctionSrv = 
                    (IAuctionItemService) ctx.getBean("auctionItemService");          
        List<AuctionItem> rawData = null;
        
        try {
            if((searchPhrase != null && selectedCategory != null) &&
                    (!searchPhrase.isEmpty() && !selectedCategory.isEmpty())) {
                rawData = auctionSrv.findByCategoryAndSearchPhrase(
                                    selectedCategory, searchPhrase, MAX_RECORDS);
                if(userId != null) {
                    auctionSrv.updateMembersRecentSearch(userId, searchPhrase);
                    recentSearches = auctionSrv.findRecentSearchesByUser(userId);
                }

            } else if(searchPhrase != null && !searchPhrase.isEmpty()) {
                rawData = auctionSrv.findBySearchPhrase(searchPhrase, MAX_RECORDS);
                if(userId != null) {
                    auctionSrv.updateMembersRecentSearch(userId, searchPhrase);
                    recentSearches = auctionSrv.findRecentSearchesByUser(userId);
                }

            } else if(selectedCategory != null && !selectedCategory.isEmpty()) {
                rawData = auctionSrv.findByCategory(selectedCategory, MAX_RECORDS);

            } else {
                rawData = auctionSrv.findAllLimited(MAX_RECORDS);
            }
        } catch(DataAccessException dae) {
            LOG.error("Data Access Exception: " + dae.getMessage());
            dae.printStackTrace();
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Search Error", "Sorry, the search failed. Please report to the webmaster."));
        } catch(Exception e) {
            LOG.error("Misc. HomeNavBean findItems exception:");
            e.printStackTrace();
        }
        
        auctionItemsFound = new ArrayList<AuctionItemDto>(rawData.size());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        AuctionItemDto dto = null;
        
        for(AuctionItem ai : rawData) {
            dto = new AuctionItemDto();
            dto.setItemId(ai.getItemId());
            dto.setTitle(ai.getTitle());
            dto.setImage1Url(ai.getImage1());
            dto.setImage2Url(ai.getImage2());
            dto.setImage3Url(ai.getImage3());
            dto.setImage4Url(ai.getImage4());
            dto.setImage5Url(ai.getImage5());
            dto.setDescription(ai.getDescription());
            dto.setThumbnail(ai.getImage1().substring(0, ai.getImage1().length()-4) + "-thumb.jpg");
            dto.setEndDate(ai.getEndDate());
            BigDecimal highestBid = 
                    auctionSrv.findHighestBidForItem(ai.getItemId());
            double bid = highestBid == null ? 0 : highestBid.doubleValue();
            BigDecimal v1 = new BigDecimal(""+bid);
            BigDecimal v2 = new BigDecimal(".10");
            v1 = v1.add(v2);
            dto.setHighBid(nf.format(bid));
            dto.setMinBid(v1.doubleValue());
            Number count = auctionSrv.findBidCountForItem(ai.getItemId());
            dto.setBidCount(count.toString());
            auctionItemsFound.add(dto);
        }
    }

    public AuctionItemDto getSelectedAuctionItemDto() {
        return selectedAuctionItemDto;
    }

    public void setSelectedAuctionItemDto(AuctionItemDto selectedAuctionItemDto) {
        LOG.debug("*** Selected Auction Item: " + selectedAuctionItemDto);
        this.selectedAuctionItemDto = selectedAuctionItemDto;
    }




}
