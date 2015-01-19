<%-- 
    Document   : Amministrazione
    Created on : 25-giu-2014, 21.39.41
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
    String notifica = (String) request.getAttribute("notifica");
    if (notifica == null) {
        notifica = "";
    }
%>

<jsp:useBean id="amministrazione" scope="request" class="amministrazione.Amministrazione"/>
<jsp:setProperty name="amministrazione" property="*"/>


<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css.css">
    </head>
    <body>
     
        <script language="JavaScript">
    function verificaInput(aggiungipizza)
    {
        if (aggiungipizza.COD.value === null || aggiungipizza.COD.value === "0" || aggiungipizza.COD.value === "") {
            alert("COD mancante.");
            aggiungipizza.COD.focus();
            return false;
        } else if (aggiungipizza.NOME.value === null || aggiungipizza.NOME.value === "") {
            alert("Pizza mancante.");
            aggiungipizza.NOME.focus();
            return false;
        } else if (aggiungipizza.DESCRIZIONE.value === null || aggiungipizza.DESCRIZIONE.value === "") {
            alert("Descrizione mancante.");
            aggiungipizza.DESCRIZIONE.focus();
            return false;
        } else if (aggiungipizza.PREZZO.value === null || aggiungipizza.PREZZO.value === "0" || aggiungipizza.PREZZO.value === "" || aggiungipizza.PREZZO.value === "null") {
            alert("Prezzo mancante.");
            aggiungipizza.PREZZO.focus();
        }else if (aggiungipizza.IMMAGINE.value === null || aggiungipizza.IMMAGINE.value === "") {
            alert("Ricorda di immettere l'immagine.");
            aggiungipizza.IMMAGINE.focus();
            return false;
            return false;
        }

        return true;
    }
</script>
        <jsp:include page="Login.jsp"/>
        <jsp:include page="Menu.jsp"/>
        <br>

    <aside>
        <a href="Gestore?action=visualizzaPrenotazioni"><img src="img/site/prenotazioniutenti.png"></a>
        <a href="Gestore?action=aggiungiPizza"><img src="img/site/aggiungipizza.png"></a>
        <br>
    </aside>

    <article>
        <br>
        <%=notifica%>
        <br><br>
        <jsp:getProperty name="amministrazione" property="amministrazione"/>
    </article>

    <footer>
        Tramontana Fabio
    </footer>
</body></html>
