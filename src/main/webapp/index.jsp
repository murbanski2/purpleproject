<%-- 
    Document   : index.jsp
    Created on : Oct 31, 2012, 4:19:41 PM
    Author     : Jim Lombardo
    Purpose    : This jsp is the start page for this applicaiton. It redirects 
                 to the real home page, a JSF page (index.xhtml). It's used
                 here because sometimes when a JSF home page has a lot of
                 content on it, weird errors can result. Redirecting to the
                 page from a jsp solves this problem. We'll also use this
                 page as a general to do list generator by using comments.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% response.sendRedirect("faces/index.xhtml"); %>

<%--
    TODO - consider using Spring/Velocity templates for email messages
    TODO - consider using JMS for async email processing
--%>