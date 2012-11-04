package edu.wctc.distjava.purpleproject.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jlombardo
 */
@Named
@RequestScoped
public class SecurityBean {

    private final Logger LOG = LoggerFactory.getLogger(HomeNavBean.class);

    public String logout() {
        String result = "/login?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            LOG.error("Failed to logout user!");
            result = "/accessDenied?faces-redirect=true";
        }

        return result;
    }
}