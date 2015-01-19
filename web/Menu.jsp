<%-- 
    Document   : Menu
    Created on : 25-giu-2014, 21.17.05
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<div id="menu">
    <br><br>
    <a href="index.jsp"><img src="img/site/index.png"></a>
    <a href="Gestore?action=cerca"><img src="img/site/catalogo.png"></a>
    <a href="Gestore?action=carrello"><img src="img/site/carrello.png"></a>

    <% String permesso = (String) session.getAttribute("permesso");
    if (permesso != null && !(permesso.equals("not_found"))) {%>
    <a href="Gestore?action=Prenotazione"><img src="img/site/prenotazioni.png"></a>
        <%}%>
        <% if (permesso != null && permesso.equals("admin")) {%>
    <a href="Gestore?action=admin"><img src="img/site/amministrazione.png"></a>
        <%}%>

    <div id="ricerca"> <hr>
        <form name="ricercaveloce" action="Gestore" method="post">
            <input type="text" name="str">
            <input type="hidden" name="action" value="ricercaveloce">
            <hr>
        </form> 
    </div> 
    <img src="img/site/banner.png">
</div>