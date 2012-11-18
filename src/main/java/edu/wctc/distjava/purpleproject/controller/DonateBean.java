package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.ICategoryService;
import edu.wctc.distjava.purpleproject.service.IUserService;
import edu.wctc.distjava.purpleproject.util.ThumbnailGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
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
public class DonateBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(DonateBean.class);
    private static final int BUFFER_SIZE = 6124; // bytes
    private static final int ONE_WEEK = 7; // days
    private transient ApplicationContext ctx; // used to get Spring beans    
    
    private String title = "";
    private String description = "";
    private Date startDate;
    private Date endDate;
    private String image1Url;
    private String image2Url;
    private String image3Url;
    private String image4Url;
    private String image5Url;
    private String sellerId;
    private String selectedCategory;

    public DonateBean() {
        initDates();
    }
    
    public String cancelDonation() {
        this.resetProperties();
        return "index";
    }

    public String saveItem() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());    
                // Needed for JSF Messages
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extCtx = FacesContext.getCurrentInstance()
                .getExternalContext();
        
        HttpServletRequest request = ((HttpServletRequest) extCtx.getRequest());
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        
        // Retrieve Spring bean
        ICategoryService catSrv = (ICategoryService) ctx.getBean("catService");        
        List<Category> catList = catSrv.findByCategoryName(selectedCategory);
        Category category = catList.get(0);
        
        AuctionItem item = new AuctionItem();
        item.setCatId(category);
        item.setTitle(title);
        item.setDescription(description);
        item.setImage1(image1Url);
        item.setImage2(image2Url);
        item.setImage3(image3Url);
        item.setImage4(image4Url);
        item.setImage5(image5Url);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        
        IUserService userSrv = (IUserService) ctx.getBean("userService");
        User user = userSrv.findByUsername(username);
        item.setSellerId(user);
        
        IAuctionItemService auctionSrv = (IAuctionItemService) ctx.getBean("auctionItemService");
        try {
            auctionSrv.save(item);
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Auction Item Donated Successfully", "Auction Item Donated Successfully"));    
            
        } catch(DataAccessException dae) {
            LOG.error("Auction item could not be saved due to: " + dae.getMessage());
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Data Storage Error", "Sorry, the auction item could not be saved."));
        }
        
        this.resetProperties();
        return "index";
    }

    public void handleFileUpload(FileUploadEvent event) {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = ((HttpServletRequest) extContext.getRequest());
        int intPort = request.getServerPort();
        String port = intPort == 80 ? "" : (":"+intPort);
        String filePathInDb = request.getScheme() + "://" 
                + request.getServerName() + port
                + request.getContextPath() + "/imgvault/"
                + event.getFile().getFileName();
        String absoluteFilePath = "/auction/imgvault/"
                + event.getFile().getFileName();
        File outFile = new File(absoluteFilePath);
        LOG.debug("*** handling File Upload: " + outFile.getPath());
        
        if(image1Url == null) {
            image1Url = filePathInDb;
        } else if(image2Url == null) {
            image2Url = filePathInDb;
        } else if(image3Url == null) {
            image3Url = filePathInDb;
        } else if(image4Url == null) {
            image4Url = filePathInDb;
        } else if(image5Url == null) {
            image5Url = filePathInDb;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[BUFFER_SIZE];

            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            String errMsg = "";
            if(image2Url == null && image3Url == null && image4Url == null && image5Url == null) {
                ThumbnailGenerator th = new ThumbnailGenerator();
                errMsg = th.createThumbnail(absoluteFilePath, 
                    absoluteFilePath.substring(0, absoluteFilePath.length()-4) + "-thumb.jpg", 200);
            }

            FacesMessage msg = new FacesMessage("Upload Success", event.getFile().getFileName()
                    + " is uploaded. " + errMsg);
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (IOException e) {
            LOG.error(e.getMessage());

            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The files were not uploaded!", "");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getImage1Url() {
        return image1Url;
    }

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public String getImage2Url() {
        return image2Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public String getImage3Url() {
        return image3Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    public String getImage4Url() {
        return image4Url;
    }

    public void setImage4Url(String image4Url) {
        this.image4Url = image4Url;
    }

    public String getImage5Url() {
        return image5Url;
    }

    public void setImage5Url(String image5Url) {
        this.image5Url = image5Url;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    private void initDates() {
        startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, ONE_WEEK);
        endDate = cal.getTime();
    }
    
    private void resetProperties() {
        title = "";
        description = "";
        this.initDates();
        image1Url = null;
        image2Url = null;
        image3Url = null;
        image4Url = null;
        image5Url = null;
        selectedCategory = null;
    }
}
