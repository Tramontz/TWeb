<%-- 
    Document   : Prenotazione
    Created on : 25-giu-2014, 22.24.22
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="prenotazione" scope="request" class="user.Prenotazione"/>
<jsp:setProperty name="prenotazione" property="*"/>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css.css">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Pizzeria TWEB</title>
    </head>
    <body>
        <jsp:include page="Login.jsp"/>
        <jsp:include page="Menu.jsp"/>
    <article>
        <br>
        <jsp:getProperty name="prenotazione" property="prenotazione"/>
    </article>

    <footer>
        Tramontana Fabio
    </footer>
</body>
</html>