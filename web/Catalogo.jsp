<%-- 
    Document   : Catalogo
    Created on : 25-giu-2014, 21.30.11
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String nome = (String) session.getAttribute("nome");
    if (nome == null) {
        nome = "";
    }

    String notifica = (String) request.getAttribute("notifica");
    if (notifica == null) {
        notifica = "";
    }
%>
 <script language="JavaScript">
            function verificaQuantita(aggiungiAlCarrello)
            {
                if (aggiungiAlCarrello.quantita.value == "0") {
                    alert("Specificare la quantità.");
                    aggiungiAlCarrello.quantita.focus();
                    return false;
                }
                return true;
            }

            function confermaCancella() {
                return confirm("Sei sicuro di voler cancellare questa pizza dal catalogo?");
            }
        </script>

<jsp:useBean id="catalogo" scope="session" class="pizze.Catalogo"/>
<jsp:setProperty name="catalogo" property="*"/>
<jsp:useBean id="carrello" scope="session" class="user.Carrello"/>
<jsp:setProperty name="carrello" property="*"/>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css.css">
    </head>
    <body>

       
        <jsp:include page="Login.jsp"/>
        <jsp:include page="Menu.jsp"/>
    <aside>
        <jsp:getProperty name="carrello" property="carrellino"/>
    </aside>
    <article>
        <br>
        <%=notifica%>
        <br>
        <form name="cerca" action="Gestore" method="post">
            <p>Nome: <input type="text" name="nome" value="<%=nome%>"></p>

            <input type="hidden" name="action" value="cerca">
            <input type="submit" value="cerca">
        </form> 
        <br>
        <form name="cerca" action="Gestore" method="post">
            <input type="hidden" name="nome" value="">
            <input type="hidden" name="action" value="cerca">
            <input type="submit" value="Azzera ricerca">
        </form>
        <br>
        <br>
        <br>
        <jsp:getProperty name="catalogo" property="risultato"/>
    </article>
    <footer>
        Tramontana Fabio
    </footer>
</body></html>
