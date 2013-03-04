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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;

/**
 *
 * @author jlombardo
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
        // do this in case of server restart and during development
        // when you don't want to wait a day for it to happen
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
        String winnerEmail = item.getWinBidderId();
        String[] ccEmails = {item.getSellerId()};
        
        try {
            getEmailService().sendEmail(winnerEmail,ccEmails);
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
