package edu.wctc.distjava.purpleproject.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
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
    
    private String noImpMsg = "Not yet Implemented";
    private CartesianChartModel categoryModel;

    public HomeNavBean() {
        createCategoryModel();
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    private void createCategoryModel() {
        categoryModel = new CartesianChartModel();

        ChartSeries donations = new ChartSeries();
//        donations.setLabel("Donations");

        donations.set("Oct", 25);
        donations.set("Nov", 125);
        donations.set("Dec", 0);
        donations.set("Jan", 0);
        donations.set("Feb", 0);
        donations.set("Mar", 0);
        donations.set("Apr", 0);
        donations.set("May", 0);
        donations.set("Jun", 0);        
        donations.set("Jul", 0);
        donations.set("Aug", 0);
        donations.set("Sep", 0);
        
        categoryModel.addSeries(donations);
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
//
//    public void setCurrentDate(Date currentDate) {
//        this.currentDate = currentDate;
//    }

    

}
