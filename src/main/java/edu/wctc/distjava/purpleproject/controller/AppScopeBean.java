package edu.wctc.distjava.purpleproject.controller;


import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.service.ICategoryService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * The Spring-managed bean is used to access global application information.
 * 
 * @author      Jim Lombardo
 * @version     1.00
 */
@Named("appBean")
@Scope("singleton")
public class AppScopeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(AppScopeBean.class);
    
    private transient ApplicationContext ctx; // used to get Spring beans
    private Date startupDate = new Date();
    private String[] categories;
    private String selectedCategory;
    
    public AppScopeBean() {
    }
    
    public String[] getCategories() {
        ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        ICategoryService catServ = (ICategoryService)ctx.getBean("catService");
        List<Category> catList = catServ.findAll();
        categories = new String[catList.size()];
        for(int i=0; i < categories.length; i++) {
            categories[i] = catList.get(i).getCategory();
        }
        
        return categories;
    }

    public String getServerInfo() {
        ServletContext servletContext = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();
        return servletContext.getServerInfo();
    }
    /**
     * Used to display the startup data/time on the home page.
     * 
     * @return current data/time
     */
    public Date getStartupDate() {
        return startupDate;
    }    

    private void setSelectedCategory(String value) {
        this.selectedCategory = value;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }
    
    
}
