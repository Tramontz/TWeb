<%-- 
    Document   : PaginaPizza
    Created on : 25-giu-2014, 21.45.53
    Author     : Tramontz
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script language="JavaScript">
    function verificaInput(modificaPizza) {

        if (modificaPizza.cod.value === null || modificaPizza.cod.value === "" || modificaPizza.cod.value === "null") {
            alert("COD mancante.");
            modificaPizza.cod.focus();
            return false;
        } else if (modificaPizza.titolo.value === null || modificaPizza.titolo.value === "") {
            alert("Nome mancante.");
            modificaPizza.nome.focus();
            return false;
        } else if (modificaPizza.descrizione.value === null || modificaPizza.descrizione.value === "") {
            alert("Descrizione mancante.");
            modificaPizza.descrizione.focus();
            return false;
        } else if (modificaPizza.prezzo.value === null || modificaPizza.prezzo.value === "0" || modificaPizza.prezzo.value === "" || modificaPizza.prezzo.value === "null") {
            alert("Prezzo mancante.");
            modificaPizza.prezzo.focus();
            return false;
        }

        return true;
    }
</script>

<jsp:useBean id="paginapizza" scope="request" class="pizze.PaginaPizza"/>
<jsp:setProperty name="paginapizza" property="*"/>
<jsp:useBean id="carrello" scope="session" class="user.Carrello"/>
<jsp:setProperty name="carrello" property="*"/>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css.css">
    </head><body>
        <jsp:include page="Login.jsp"/>
        <jsp:include page="Menu.jsp"/>

    <aside>
        <jsp:getProperty name="carrello" property="carrellino"/>
    </aside>
    <article>
        <div id="paginapizza">
            <jsp:getProperty name="paginapizza" property="risultato"/>
        </div>
    </article>

    <footer>
        Tramontana Fabio
    </footer>

</body>
</html>