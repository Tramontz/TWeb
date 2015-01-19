<%-- 
    Document   : Login
    Created on : 25-giu-2014, 20.55.26
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script language="JavaScript">
    function verifica(login)
    {
        // Controlla la presenza dei campi
        if (login.usr.value === "") {
            alert("Nome utente obbligatorio.");
            login.usr.focus();
            return false;
        }
        if (login.psw.value === "") {
            alert("Password obbligatoria.");
            login.psw.focus();
            return false;
        }
        return true;
    }
</script>

<header>
    <div id="login">
        <% String user = (String) session.getAttribute("username");
    if (user == null) {%>
        <br><br>
        <form name="login" action="Gestore" onSubmit="return verifica(this);" method="post">
            <table>
                <tr><td>Username:</td><td> <input type="text" name="usr"></td></tr>
                <tr><td>Password :</td> <td><input type="password" name="psw"></td></tr>
            </table>
            <input type="hidden" name="action" value="login">
            <input type="submit" value="login">

        </form>
        <%} else {%>
        <br><br><font color="white"> Ciao <%=user%> </font><br><br>
        <a href="Gestore?action=logout"><img src="img/site/esci.png" alt="Esci"></a>
            <%}%>
    </div>
</header>