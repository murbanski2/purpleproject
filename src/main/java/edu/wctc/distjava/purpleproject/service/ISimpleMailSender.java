package edu.wctc.distjava.purpleproject.service;

import edu.wctc.distjava.purpleproject.domain.User;
import org.springframework.mail.MailException;

/**
 * The general contract for Spring SimpleMailSender implementations.
 * @author jlombardo
 */
public interface ISimpleMailSender {

    /**
     * Uses Spring's SimleMailMessage technique to send a verification email
     * to the candidate member who attempted registration on Bit Bay. This
     * email is necessary to prevent robots or scammers from registering. A
     * base64 encoded username is generated to hide details of the key used
     * by the servlet to identify the user.
     *
     * @param user - the candidate member who is the target of the email
     */
    void sendEmail(String userEmail, String[] ccEmails) throws MailException;



}
