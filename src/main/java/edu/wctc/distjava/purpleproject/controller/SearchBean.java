package edu.wctc.distjava.purpleproject.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author jlombardo
 */
@Named
@RequestScoped
public class SearchBean {
    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    
}
