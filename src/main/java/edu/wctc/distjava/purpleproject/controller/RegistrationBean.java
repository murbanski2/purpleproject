package edu.wctc.distjava.purpleproject.controller;

import edu.wctc.distjava.purpleproject.domain.Authority;
import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.ISimpleMailSender;
import edu.wctc.distjava.purpleproject.service.IUserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * This Spring-managed JSF/CDI bean handles the member registration process from
 * provision of credentials, to acceptance of terms of service, to the sending
 * of an email for verification purposes, and finally to acceptance and
 * confirmation of member registration. 
 * <P> 
 * Because this process spans several
 * pages, the class uses Session scope. Note that we cannot use CDI
 * ConverstaionScope because Spring does not currently support it with CDI
 * (rumored to be fixed in next version).
 *
 * @author Jim Lombardo
 * @version 1.00
 */
@Named
@Scope("session")
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(RegistrationBean.class);
    private transient ApplicationContext ctx; // used to get Spring beans
    private String username;
    private String password;
    private String hash;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private Map<String, String> abbrevMap;
    private String zip;
    private String phone;
    private String firstName;
    private String lastName;

    public RegistrationBean() {
        // DO NOT initialize ApplicationContext here. Spring creates
        // proxy objects so you won't have direct access to this constructor.
    }

    /**
     * Handles POST of member credentials, check for existing username, hashing
     * of the password, and finally redirects to agreement page.
     *
     * @return the destination page
     */
    public String doAgreement() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());

        // Retrieve Spring bean
        IUserService userSrv = (IUserService) ctx.getBean("userService");
        // Needed for JSF Messages
        FacesContext context = FacesContext.getCurrentInstance();

        // Determine if user already exists
        User curUser = userSrv.findByUsername(username);
        if (curUser != null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "User Already Exists", "Sorry, that user email is already in use."));

            return "registrationForm"; // go back to form to try again
        }

        // Prepare salted hash for password here, so as not to
        // overtax the doRegistration method below
        hash = encodeSha512(password, username);
        return "registrationAgreement";
    }

    /**
     * Should the visitor decline the terms of service on the agreement page,
     * this ends the conversation and redirects to the home page.
     *
     * @return the destination page
     */
    public String doDecline() {
        return "index"; // back to home page
    }

    /**
     * Handles part one of the registration process, which is to record the user
     * credentials in the database, including a salted hash of the password, but
     * disable the account until the email verification process is completed.
     * <P> Note that the username is the primary key in the db and, therefore,
     * must be unique. A query is performed to check whether the requested
     * username is already being used, and if so, redirect back with a message
     * stating this.
     *
     * @return the destination page
     */
    public String doRegistration() {
        ctx = FacesContextUtils.getWebApplicationContext(
                FacesContext.getCurrentInstance());

        // Retrieve Spring bean
        IUserService userSrv = (IUserService) ctx.getBean("userService");
        // Needed for JSF Messages
        FacesContext context = FacesContext.getCurrentInstance();

        User user = new User();
        user.setUsername(username);
        user.setPassword(hash);
        user.setEnabled(false); // don't want enabled until email verified!
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        List<Authority> auths = new ArrayList<Authority>();
        Authority auth = new Authority();
        auth.setAuthority("ROLE_MEMBER");
        auths.add(auth);
        user.setAuthoritiesCollection(auths);
        auth.setUsername(user);

        try {
            user = userSrv.save(user);
        } catch (DataAccessException dae) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Storage Failure", "Sorry, your registration information could "
                    + "not be saved at this time. Please try again later. "
                    + "Thanks for your patience."));

            LOG.error("*** Registration could not be saved to database "
                    + "due to: " + dae.getMessage());

            return "registrationAgreement";
        }

        // Now send the verification email using Spring's help
        // @see http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mail.html#mail-introduction
        ISimpleMailSender emailSender =
                (ISimpleMailSender) ctx.getBean("emailSender");

        try {
            emailSender.sendUserEmailVerification(user);
        } catch (MailException ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Email Failure", "Sorry, the verification email could not be "
                    + "sent. Please notify the webmaster at "
                    + "bitbay.webmaster@gmail.com and we'll complete the "
                    + "process for you. Thanks for your patience."));

            return "registrationAgreement";
        }

        return "registrationConfirmed";
    }

    /*
     * Creates a salted SHA-512 hash composed of password (pwd) and 
     * salt (username).
     */
    private String encodeSha512(String pwd, String salt) {
        ShaPasswordEncoder pe = new ShaPasswordEncoder(512);
        pe.setIterations(1024);
        String hash = pe.encodePassword(pwd, salt);

        return hash;
    }

    /**
     * @return username (email address)
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username (email address)
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, String> getAbbrevMap() {
        return abbrevMap;
    }

    @PostConstruct
    public void init() {
        abbrevMap = new LinkedHashMap<String, String>();
        abbrevMap.put("ALABAMA", "AL");
        abbrevMap.put("ALASKA", "AK");
        abbrevMap.put("ARIZONA", "AZ");
        abbrevMap.put("ARKANSAS", "AR");
        abbrevMap.put("CALIFORNIA", "CA");
        abbrevMap.put("COLORADO", "CO");
        abbrevMap.put("CONNECTICUTT", "CT");

        abbrevMap.put("DELAWARE", "DE");
        abbrevMap.put("DISTRICT OF COLUMBIA", "DC");
        abbrevMap.put("FLORIDA", "FL");
        abbrevMap.put("GEORGIA", "GA");
        abbrevMap.put("HAWAII", "HI");
        abbrevMap.put("IDAHO", "ID");

        abbrevMap.put("ILLINOIS", "IL");
        abbrevMap.put("INDIANA", "IN");
        abbrevMap.put("IOWA", "IA");
        abbrevMap.put("KANSAS", "KS");
        abbrevMap.put("KENTUCKY", "KY");
        abbrevMap.put("LOUISIANA", "LA");

        abbrevMap.put("MAINE", "ME");
        abbrevMap.put("MARYLAND", "MD");
        abbrevMap.put("MASSACHUSETTS", "MA");
        abbrevMap.put("MICHIGAN", "MI");
        abbrevMap.put("MINNESOTA", "MN");

        abbrevMap.put("MISSISSIPPI", "MS");
        abbrevMap.put("MISSOURI", "MO");
        abbrevMap.put("MONTANA", "MT");
        abbrevMap.put("NEBRASKA", "NE");
        abbrevMap.put("NEVADA", "NV");
        abbrevMap.put("NEW HAMPSHIRE", "NH");

        abbrevMap.put("NEW JERSEY", "NJ");
        abbrevMap.put("NEW MEXICO", "NM");
        abbrevMap.put("NEW YORK", "NY");
        abbrevMap.put("NORTH CAROLINA", "NC");
        abbrevMap.put("NORTH DAKOTA", "ND");

        abbrevMap.put("OKLAHOMA", "OK");
        abbrevMap.put("OREGON", "OR");
        abbrevMap.put("PENNSYLVANIA", "PA");
        abbrevMap.put("PUERTO RICO", "PR");
        abbrevMap.put("RHODE ISLAND", "RI");
        abbrevMap.put("SOUTH CAROLINA", "SC");
        abbrevMap.put("SOUTH DAKOTA", "SD");

        abbrevMap.put("TENNESSEE", "TN");
        abbrevMap.put("TEXAS", "TX");
        abbrevMap.put("UTAH", "UT");
        abbrevMap.put("VERMONT", "VT");
        abbrevMap.put("VIRGINIA", "VA");
        abbrevMap.put("WASHINGTON", "WA");
        abbrevMap.put("WEST VIRGINIA", "WV");
        abbrevMap.put("WISCONSIN", "WI");
        abbrevMap.put("WYOMING", "WY");
    }
    
    
}
