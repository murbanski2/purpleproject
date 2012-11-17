package edu.wctc.distjava.purpleproject.controller;


import edu.wctc.distjava.purpleproject.domain.Category;
import edu.wctc.distjava.purpleproject.service.ICategoryService;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
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
    private CartesianChartModel chartModel;    
    private String[] months = {
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    };
    private int m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12;
    
    public AppScopeBean() {
        initChartMonths();
        createChardModel();
    }
    
    public String[] getCategories() {
        if(categories == null) {
            initCategories();
        }
        
        return categories;
    }

    public void initCategories() throws BeansException {
        ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        ICategoryService catServ = (ICategoryService)ctx.getBean("catService");
        List<Category> catList = catServ.findAll();
        categories = new String[catList.size()];
        for(int i=0; i < categories.length; i++) {
            categories[i] = catList.get(i).getCategory();
        }
    }
    

    public CartesianChartModel getCategoryModel() {
        return chartModel;
    }

    public final void createChardModel() {
        chartModel = new CartesianChartModel();

        ChartSeries donations = new ChartSeries();

        donations.set(months[m1], 0);
        donations.set(months[m2], 0);
        donations.set(months[m3], 0);
        donations.set(months[m4], 0);
        donations.set(months[m5], 25);
        donations.set(months[m6], 75);
        donations.set(months[m7], 125);
        donations.set(months[m8], 0);
        donations.set(months[m9], 0);        
        donations.set(months[m10], 0);
        donations.set(months[m11], 0);
        donations.set(months[m12], 0);
        
        chartModel.addSeries(donations);
    }
    
    private void initChartMonths() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, -6);
        m1 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m2 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m3 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m4 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m5 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m6 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m7 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m8 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m9 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m10 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m11 = today.get(Calendar.MONTH);
        today.add(Calendar.MONTH, 1);
        m12 = today.get(Calendar.MONTH);
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
    
    
}
