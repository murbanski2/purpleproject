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
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

/**
 * This Spring-managed bean is configured in applicationContext.xml and does
 * the job of sending the email message for new member verification purposes.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
@Service("emailVerificationSender")
@Qualifier("verification")
public class EmailVerificationSender implements ISimpleMailSender, Serializable {
    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(EmailVerificationSender.class);
    
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
    public void sendEmail(String userEmail, Object data) throws MailException {
        // Create a Base64 encode of the username
        byte[] encoded = Base64.encode(userEmail.getBytes()); 
        String base64Username = new String(encoded);
        
        // Create a thread safe "copy" of the template message and customize it
        // See Spring config in applicationContext.xml
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(userEmail);

        msg.setText("Thank you for registering with bitBay(tm), the unique "
                + "auction site that benefits WCTC's IT student club. This email is "
                + "being sent to you to verify your intent to join bitBay. "
                + "To complete the registration process for user email [" 
                + userEmail + "], please click on the link below."
                + "\n\nCAUTION: if you did not register with bitBay, somebody "
                + "else is trying use your email to scam the system. Please "
                + "do not click on the link below unless you intend to confirm "
                + "your registraiton with bitBay.\n\n"
                + "Here's the link to complete the registraiton process: \n\n"
                + "http://localhost:8080/bitbay/regVerify.do?id=" + base64Username);
        
        try {
            mailSender.send(msg);
        } catch(NullPointerException npe) {
            throw new MailSendException(
                    "Email send error from EmailVerificationSender");
        }
    }
    
    
}
