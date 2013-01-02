package edu.wctc.distjava.purpleproject.controller;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 *
 * @author jlombardo
 */
@Singleton
public class AuctionEndManager {
    private final Logger LOG = LoggerFactory.getLogger(AuctionEndManager.class);
    private transient ApplicationContext ctx; // used to get Spring beans    
    
//    @PostConstruct
//    public void init() {
//        ctx = FacesContextUtils.getWebApplicationContext(
//                FacesContext.getCurrentInstance());
//    }
//    
//    
//    @Schedule(second="*/5", minute="*",hour="*", persistent=false)
//    public void doWork(){
//        LOG.debug("**** timer: output");
//    }

}
