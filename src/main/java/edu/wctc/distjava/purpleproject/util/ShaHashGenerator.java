package edu.wctc.distjava.purpleproject.util;

import java.security.MessageDigest;

/**
 * Use this class to create SHA-256 hashed passwords that can be stored in
 * the database users table.
 * 
 * @author jlombardo
 */
public class ShaHashGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(sha256("member")); // raw password before hashing
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
