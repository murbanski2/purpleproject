package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.service.IAuctionItemService;
import edu.wctc.distjava.purpleproject.service.ISimpleMailSender;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.interceptor.Interceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.mail.MailSendException;

/**
 * This EJB is responsible for checking daily whether or not there are any
 * auctions ending. When found a countdown timer is started for each one, with
 * the end result being that an email message is sent exactly at the item's
 * end date and time, to the winner, with a copy to the donator.
 * <P>
 * NOTE: a Spring intercepter is used to allow auto injection of Spring-managed
 * beans. The Qualifier determines the implementation of the Strategy object.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Singleton
@Startup
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class AuctionEndManager {

    private final Logger LOG = LoggerFactory.getLogger(AuctionEndManager.class);
    private List<AuctionItem> expiringItems;
    @Resource
    private TimerService timerService;
    @Autowired
    IAuctionItemService auctionSrv;
    @Autowired
    @Qualifier("winner")
    private ISimpleMailSender emailService;
    
    @PostConstruct
    public void init() {
        createCountdownTimers();
    }

    @Schedule(minute="0", hour="0", dayOfWeek="*", persistent=false)
    public void scheduleCountdownTimersForExpiringItems() {
        createCountdownTimers();
    }

    @Timeout
    public void handleExpiringItem(Timer timer) {
        AuctionItem item = (AuctionItem) timer.getInfo();
        LOG.debug("*** handled expiring item: " + item.getTitle());
        
        try {
            getEmailService().sendEmail(item.getWinBidderId(),item);
        } catch (MailSendException ex) {
            LOG.debug(
                "Winner email could not be sent due to: "
                + ex.getMessage());
        }
    }

    public IAuctionItemService getAuctionSrv() {
        return auctionSrv;
    }

    public void setAuctionSrv(IAuctionItemService auctionSrv) {
        this.auctionSrv = auctionSrv;
    }

    private void createCountdownTimers() throws IllegalStateException, IllegalArgumentException, EJBException {
        LOG.debug("*** expire pool created");
        
        // Get a list of the expiring items
        expiringItems = auctionSrv.findByEndDatesToday();

        // Now create a programmable countdown timer for each item
        for (AuctionItem item : expiringItems) {
            Timer timer = timerService
                    .createSingleActionTimer(item.getEndDate(),
                    new TimerConfig(item, false));
        }
    }

    public ISimpleMailSender getEmailService() {
        return emailService;
    }

    public void setEmailService(ISimpleMailSender emailService) {
        this.emailService = emailService;
    }

}
