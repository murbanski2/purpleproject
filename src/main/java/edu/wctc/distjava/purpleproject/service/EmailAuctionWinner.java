package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.AuctionItem;
import edu.wctc.distjava.purpleproject.domain.Bid;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * This Spring-managed bean is configured in applicationContext.xml and does
 * the job of sending email messages to winner and donator.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Service("emailAuctionWinner")
@Qualifier("winner")
public class EmailAuctionWinner implements ISimpleMailSender, Serializable {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient final Logger LOG = LoggerFactory.getLogger(EmailAuctionWinner.class);
    
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
    @Autowired
    IAuctionItemService auctionSrv;    
    
    @Override
    public void sendEmail(String userEmail, Object data) throws MailException {
        // Collect auction info
        AuctionItem item = (AuctionItem)data;
        Bid bd = auctionSrv.findHighestBidForItem(item.getItemId());
        
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(userEmail);
        
        // Override applicationContext.xml
        msg.setSubject("bitBay Auction Winning Bid Notice");
        // Also let the donator know
        msg.setCc(item.getSellerId().getUserName());
        
        // set the messsage
        msg.setText("Congratulations, you had the winning bid for bitBay "
                + "auction item: " + item.getTitle() + ".\n\nYour winning bid "
                + "of $" + bd.getAmount().toString() + " should be paid by "
                + "cash or check to BIT Club by mail or in person.\n\nOnce "
                + "payment is received we'll provide you with the contact "
                + "information for the person who donated the item so that "
                + "you can make arrangements for delivery of the item.");         
        
        try {
            mailSender.send(msg);
        } catch(NullPointerException ex) {
            throw new MailSendException(ex.getMessage());
        }
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SimpleMailMessage getTemplateMessage() {
        return templateMessage;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
    
    
}
