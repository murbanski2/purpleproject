package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.User;
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
    private final Logger LOG = LoggerFactory.getLogger(EmailAuctionWinner.class);
    
    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage templateMessage;
    
    /**
     * Uses Spring's SimleMailMessage technique to send a verification email
     * to the candidate member who attempted registration on Bit Bay. This 
     * email is necessary to prevent robots or scammers from registering. A 
     * base64 encoded username is generated to hide details of the key used
     * by the servlet to identify the user.
     * 
     * @param user - the candidate member who is the target of the email
     */
    @Override
    public void sendEmail(String userEmail, String[] ccEmails) throws MailException {
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(userEmail);
        
        // Override applicationContext.xml
        msg.setSubject("Bit Bay Winning Bid Notice");
        // Also let the donator know
        msg.setCc(ccEmails);
        
        // set the messsage
        msg.setText("Congratulations, you had the winning bid. ");         
        
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
