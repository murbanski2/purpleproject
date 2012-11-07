package edu.wctc.distjava.purpleproject.util;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * This app will generate a salted, SHA-512 hashed password based on the 
 * username and password field values in the users table in the db. Use this 
 * to initially seed your database with the hashed passwords.
 * 
 * @author jlombardo
 */
public class ShaHashGeneratorApp {

    /**
     * @param args the command line arguments - not used.
     */
    public static void main(String[] args) {
        String salt = "member@isp.com"; // username field in db
        String password = "member"; // password field in db
        System.out.println(sha512(password,salt));
    }

    public static String sha512(String pwd, String salt) {

            ShaPasswordEncoder pe = new ShaPasswordEncoder(512);
            pe.setIterations(1024);
            String hash = pe.encodePassword(pwd, salt);

            return hash;
     
    }
}
