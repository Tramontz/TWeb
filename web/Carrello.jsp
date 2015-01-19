<%-- 
    Document   : Carrello
    Created on : 25-giu-2014, 22.25.51
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
<script language=JavaScript>
    function verificaQuantita(rimuoviDalCarrello)
    {
        if (rimuoviDalCarrello.quantita.value === "0") {
            alert("Specificare la quantità.");
            rimuoviDalCarrello.quantita.focus();
            return false;
        }
        return true;
    }
</script>

<jsp:useBean id="carrello" scope="session" class="user.Carrello"/>

<jsp:setProperty name="carrello" property="*"/>


<link rel="stylesheet" type="text/css" href="css.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pizzeria TWEB</title>
</head>

<body>
    <jsp:include page="Login.jsp"/>
    <jsp:include page="Menu.jsp"/>
    <br>
<article>
    <%=notifica%>

    <h3>Il tuo carrello:</h3>
    <br><br>
    <div id="carrello">
        <jsp:getProperty name="carrello" property="carrello"/>
    </div>
    <br>
    <p>Totale: <jsp:getProperty name="carrello" property="totale"/></p>
</article>

<footer>
    Tramontana Fabio
</footer>
</body>
</html>