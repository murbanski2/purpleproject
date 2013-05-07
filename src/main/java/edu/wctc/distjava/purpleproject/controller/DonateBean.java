package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.ICategoryService;
import edu.wctc.distjava.purpleproject.service.IUserService;
import edu.wctc.distjava.purpleproject.util.FacesUtils;
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
import java.util.UUID;
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
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import edu.wctc.distjava.purpleproject.domain.PopularItemDto;
import java.util.Arrays;
import javax.faces.event.ActionEvent;

/**
 *
 * @author jlombardo
 */
@Named
@Scope("session")
public class DonateBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
     * Note that @SuppressWarnings is only used by a source code analyzer
     * that I use caled "FindBugs". You don't need this unless you do to.
     */
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(DonateBean.class);
    private static final int ONE_WEEK = 7; // days
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
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
    private int fileCount;
    private String selectedEditType;
    private List<String> findItemTypes;
    // re-purpose this for edit item search results page
    private List<AuctionItem> editItemsFound;
    private String searchPhrase;

    public DonateBean() {
        initDates();
        findItemTypes = 
            Arrays.asList(
                "Last 7 Days", "Last 30 Days", "Last 60 Days", "Last 90 Days");
    }
    
    public String cancelDonation() {
        this.resetProperties();
        return "index";
    }
    

    public void doItemSearchForEdit(ActionEvent event) {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());
        IAuctionItemService aucSrv =
                (IAuctionItemService) ctx.getBean("auctionItemService");

        if (selectedEditType.equals("Last 7 Days")) {
            editItemsFound = 
                    aucSrv.findItemsToEdit(searchPhrase, "Last 7 Days");
        } else if(selectedEditType.equals("Last 30 Days")) {
            editItemsFound = 
                    aucSrv.findItemsToEdit(searchPhrase, "Last 30 Days");
        } else if(selectedEditType.equals("Last 60 Days")) {
            editItemsFound = 
                    aucSrv.findItemsToEdit(searchPhrase, "Last 60 Days");
        } else {
            editItemsFound = 
                    aucSrv.findItemsToEdit(searchPhrase, "Last 90 Days");
        }
    }    

    public String saveItem() {
        // Spring context
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());   
        
        // Give JSF access to HttpServletRequest
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
            FacesUtils.addSuccessMessage("Auction Item Donated Successfully. Thank You!");   
            
        } catch(DataAccessException dae) {
            LOG.error("Auction item could not be saved due to: " + dae.getMessage());
            FacesUtils.addErrorMessage("Could not save auction item due to: " 
                    + dae.getMessage());
        }
        
        this.resetProperties();
        return "index";
    }
    
    
    private String renameFile(FileUploadEvent event) {
        // For security we want to change the file name
        String fileName = event.getFile().getFileName();
        String fileExt = fileName.substring(fileName.length()-4);
        fileName = UUID.randomUUID().toString().replaceAll("-", "");
        fileName += fileExt;
        return fileName;
    }

    public void handleFileUpload(FileUploadEvent event) {
        final int BUFFER_SIZE = 6124; // bytes
        final String DB_ROOT = "/imgvault/";
        final String OS_ROOT ="/auction/imgvault/";
        
        String fileName = renameFile(event);
        
        String filePathInDb = DB_ROOT + fileName;
        String absoluteFilePath = OS_ROOT + fileName;
        
        File outFile = new File(absoluteFilePath);
        
        // In the unlike event that the filename already exists, recreate...
        while(outFile.exists()) {
            fileName = renameFile(event);
            filePathInDb = DB_ROOT + fileName;
            absoluteFilePath = OS_ROOT + fileName;
            outFile = new File(absoluteFilePath);
        }
        
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
        fileCount++;

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            // Limit the count of files uploaded.
            // In Primefaces 3.5+ this won't be necessary
            if(fileCount > 5) {
                FacesUtils.addErrorMessage("You've exceeded the 5 file limit!");
                return;
            }          
            
            // Now write the file using the new file name
            fileOutputStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead;
            inputStream = event.getFile().getInputstream();
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bytesRead);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            /*
             * We use the ThumbnailGenerator for two purposes:
             * (1) It recreates and resizes the original image, which
             * saves space on disk but also adds safety because any hackers who
             * try to imbed non-image stuff into the file will be twarted.
             * (2) To create an actual thumbnail
             */
            ThumbnailGenerator th = new ThumbnailGenerator();
            String errMsg = 
                    th.createThumbnail(absoluteFilePath, absoluteFilePath, 640);            
            
            // Create thumbnail from first image only, on first upload only
            if(image2Url == null && image3Url == null 
               && image4Url == null && image5Url == null) {
                
                errMsg = th.createThumbnail(absoluteFilePath, 
                    absoluteFilePath.substring(0, 
                        absoluteFilePath.length()-4) + "-thumb.jpg", 200);
            }

            if(errMsg.length() == 0) {
            FacesUtils.addSuccessMessage(event.getFile().getFileName() 
                    + " was uploaded.");
            } else {
                FacesUtils.addErrorMessage("Sorry, the file "
                        + event.getFile().getFileName() 
                        + " could not be uploaded because "
                        + errMsg);
            }

        } catch (IOException e) {
            try {
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                FacesUtils.addErrorMessage(
                    "Your file could not be uploaded because "
                    + e.getMessage());
            }

            LOG.error(e.getMessage());

            FacesUtils.addErrorMessage(
                    "Your file could not be uploaded because "
                    + e.getMessage());
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

    @SuppressWarnings
    public Date getStartDate() {
        return startDate;
    }

    @SuppressWarnings
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @SuppressWarnings
    public Date getEndDate() {
        return endDate;
    }

    @SuppressWarnings
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
        fileCount = 0;
    }

    public List<String> getFindItemTypes() {
        return findItemTypes;
    }

    public void setFindItemTypes(List<String> findItemTypes) {
        this.findItemTypes = findItemTypes;
    }

    public String getSelectedEditType() {
        return selectedEditType;
    }

    public void setSelectedEditType(String selectedEditType) {
        this.selectedEditType = selectedEditType;
    }

    public List<AuctionItem> getEditItemsFound() {
        return editItemsFound;
    }

    public void setEditItemsFound(List<AuctionItem> editItemsFound) {
        this.editItemsFound = editItemsFound;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

}
