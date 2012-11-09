<%-- 
    Document   : servletError
    Created on : Nov 9, 2012, 2:48:39 PM
    Author     : jlombardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <h1>Sorry, there was a Database Connection Error</h1>
        <p>The error was: <%= request.getAttribute("errMsg").toString()%></p>
        
    </body>
</html>
