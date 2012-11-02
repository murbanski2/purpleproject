package edu.wctc.distjava.purpleproject.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

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
    private Date startupDate = new Date();
    private String[] categories= {
            "All Categories",
            "Hardware",
            "Softwware"
        };
    private String selectedCategory;
    
//    private transient ApplicationContext ctx;
    
    public AppScopeBean() {
//        ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
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

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    private void setSelectedCategory(String value) {
        this.selectedCategory = value;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }
    
    
}
