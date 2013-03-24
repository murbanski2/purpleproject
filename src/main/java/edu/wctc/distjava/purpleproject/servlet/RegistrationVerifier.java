/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wctc.distjava.purpleproject.servlet;

import edu.wctc.distjava.purpleproject.domain.User;
import edu.wctc.distjava.purpleproject.service.IUserService;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class is used to process the verification link sent via email to 
 * prospective new members of Bit Bay. It does this by converting the
 * base64 representation of the user email address back to a String and then
 * using that String to lookup the user. If the user exists, the 'enabled'
 * status in the database is set to true, else the user is redirected to
 * an error page.
 * 
 * @author  Jim Lombardo
 * @version 1.00
 */
public class RegistrationVerifier extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String errMsg = "";
        String destination = "/faces/registrationVerified.xhtml";
        
        try {
            String id = request.getParameter("id");
            byte[] decoded = Base64.decode(id.getBytes());
            String username = new String(decoded);
            
            /*
             * Retrieving the Spring ApplicationContext this way provides access
             * to all spring beans and the ServletContext object as well, if needed.
             * For this coce block, however, it is not needed and we could have
             * just used util.SpringContext to retrieve the beans.
             */
            WebApplicationContext ctx =
                   WebApplicationContextUtils
                   .getWebApplicationContext(this.getServletContext());

            IUserService userSrv = (IUserService)ctx.getBean("userService");
            User user = userSrv.findByUsername(username);
            if(user == null) {
                throw new DataSourceLookupFailureException("Sorry, that user is not in our system");
            }
            user.setEnabled(true);
            user.setDateVerified(new Date());
            userSrv.saveAndFlush(user);
                        
        } catch(DataAccessException dae) {
            errMsg = "ERROR: " + dae.getLocalizedMessage();
            request.setAttribute("errMsg", errMsg);
            destination = "/faces/servletError.xhtml";
        }
        
                    
        RequestDispatcher dispatcher =
                    getServletContext().getRequestDispatcher(destination);
                dispatcher.forward(request, response);     
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
