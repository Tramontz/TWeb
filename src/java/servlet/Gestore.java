/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.sql.*;
import java.lang.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amministrazione.Amministrazione;
import pizze.Catalogo;
import pizze.PaginaPizza;
import user.User;
import user.Carrello;
import user.Prenotazione;

/**
 *
 * Servlet implementation class Gestore
 */
@WebServlet("/Gestore")
public class Gestore extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Gestore() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ServletException e1 = new ServletException(e.getMessage());
            throw e1;
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        analyze(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        analyze(request, response);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException {
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(page);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        rd.forward(request, response);
    }

    protected void analyze(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        switch (action) {
            case ("login"): {
                String usr = request.getParameter("usr");
                String psw = request.getParameter("psw");
                String permesso = User.checkLogin(usr, psw);
                if (!(permesso.equals("not_found"))) {
                    session.setAttribute("username", usr);
                    session.setAttribute("permesso", permesso);
                    forward(request, response, "/index.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            /*
             *  IO VOGLIO CHE DOPO IL TENTATIVO DI COMPRARE SENZA PERMESSO
             *  UNA VOLTA AUTENTICATO
             *  IL CARRELLO RIMANGA ATTIVO
             */
            case ("logout"): {
                session.invalidate();
                forward(request, response, "/index.jsp");
            }
            break;
            case ("aggiungiAlCarrello"): {
                String cod = request.getParameter("COD");
               // String nome = request.getParameter("nome");
                int quantita = Integer.parseInt(request.getParameter("quantita"));
                 Carrello carrello = (Carrello)session.getAttribute("carrello");                
              
               if (carrello == null) {
                    carrello = new Carrello();
                }
                
                if ((carrello.aggiungi(cod, quantita)) == false) {
                    request.setAttribute("notifica", "impossibile aggiungere pizza al carrello");
                }
                //session.setAttribute("nome", nome);
              //  session.setAttribute("carrello", carrello);
                forward(request, response, "/Catalogo.jsp");
            }
            break;
            case ("cerca"): {
                try {
                    Catalogo catalogo;
                    catalogo = (Catalogo) session.getAttribute("catalogo");
                    if (catalogo == null) {
                        catalogo = new Catalogo();
                    }
                    String nome = request.getParameter("nome");
                    String permesso = (String) session.getAttribute("permesso");
                    if (permesso != null && permesso.equals("admin")) {
                        catalogo.search(nome, true);
                    } else {
                        catalogo.search(nome, false);
                    }
                    session.setAttribute("permesso", permesso);
                    session.setAttribute("nome", nome);
                    session.setAttribute("catalogo", catalogo);
                    forward(request, response, "/Catalogo.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            break;
            case ("rimuoviDalCarrello"): {
                String cod = request.getParameter("cod");
                int quantita = Integer.parseInt(request.getParameter("quantita"));
                Carrello carrello = (Carrello) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new Carrello();
                }
                if ((carrello.rimuovi(cod, quantita)) == false) {
                    request.setAttribute("notifica", "impossibile rimuovere pizza dal carrello!");
                } else {
                    request.setAttribute("notifica", "");
                }
                session.setAttribute("carrello", carrello);
                forward(request, response, "/Carrello.jsp");
            }
            break;
            case ("svuota"): {
                Carrello carrello = (Carrello) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new Carrello();
                }
                carrello.svuota();
                session.setAttribute("carrello", carrello);
                forward(request, response, "/Carrello.jsp");
            }
            break;
            case ("carrello"): {
                forward(request, response, "/Carrello.jsp");
            }
            break;
            case ("buy"): {
                String permesso = (String) session.getAttribute("permesso");
                String username = (String) session.getAttribute("username");
                Carrello carrello = (Carrello) session.getAttribute("carrello");
               if (permesso == null || carrello == null || permesso.equals("not_found") || username == null) {
                    forward(request, response, "/ErroreLogin.jsp");
               } else {
                    Prenotazione pre = (Prenotazione) request.getAttribute("prenotazione");
                    if (pre == null) {
                        pre = new Prenotazione();                        
                    }
                    boolean b=pre.aggiungiPrenotazione(carrello, username);            
                    session.setAttribute("username", username);
                    session.setAttribute("permesso", permesso);
                    request.setAttribute("prenotazione", pre);
                    session.setAttribute("carrello", carrello);
                    forward(request, response, "/Prenotazione.jsp");
                }
            }
            break;
            case ("admin"): {
                String utente = (String) request.getParameter("utente");
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    Amministrazione admin = new Amministrazione();
                    admin.visualizzaPrenotazioni(utente);
                    request.setAttribute("amministrazione", admin);
                    forward(request, response, "/Amministrazione.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            case ("visualizzaPrenotazioni"): {
                String utente = (String) request.getParameter("utente");
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    Amministrazione admin = new Amministrazione();
                    admin.visualizzaPrenotazioni(utente);
                    request.setAttribute("utente", utente);
                    request.setAttribute("amministrazione", admin);
                    forward(request, response, "/Amministrazione.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            case ("evadiOrdine"): {
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    int cod = Integer.parseInt((String) request.getParameter("cod"));
                    String who = (String) request.getParameter("who");
                    Amministrazione admin = new Amministrazione();
                    admin.evadiOrdine(cod);
                    admin.visualizzaPrenotazioni(who);
                    request.setAttribute("amministrazione", admin);
                    forward(request, response, "/Amministrazione.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            case ("cancellaOrdine"): {
                String cod = (String) request.getParameter("cod");
                String username = (String) session.getAttribute("username");
                Prenotazione pre = (Prenotazione) request.getAttribute("prenotazione");
                if (pre == null) {
                    pre = new Prenotazione();
                }
                if (username == null || username.equals("")) {
                    forward(request, response, "/ErroreLogin.jsp");
                } else {
                    pre.cancellaOrdine(username, cod);
                    request.setAttribute("prenotazione", pre);
                    request.setAttribute("username", username);
                    forward(request, response, "/Prenotazione.jsp");
                }
            }
            break;
            case ("aggiungiPizza"): {
                System.out.println("1");
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    Amministrazione admin = new Amministrazione();
                    String cod = (String) request.getParameter("COD");
                    if (cod == null || cod.equals("")) {                        
                        admin.visualizzaAggiungiPizza();
                        
                    } else {
                        
                        String nome = request.getParameter("NOME");
                        String descrizione = request.getParameter("DESCRIZIONE");
                        double prezzo =  Double.parseDouble(request.getParameter("PREZZO"));
                        String immagine = request.getParameter("IMMAGINE");
                        if (nome != null && descrizione != null && prezzo!=0 && immagine!=null) {
                            boolean success = admin.aggiungiPizza(cod, nome, descrizione, prezzo, immagine);
                            if (success) {
                                request.setAttribute("notifica", "Pizza aggiunta con successo: "+nome );
                            } else {
                                request.setAttribute("notifica", "Impossibile aggiungere Pizza, controllare i valori inseriti");
                            }
                        }

                    }
                    request.setAttribute("amministrazione", admin);
                    forward(request, response, "/Amministrazione.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;

            case ("visualizzaModificaPizza"): {
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    PaginaPizza pL = new PaginaPizza();
                    String cod = (String) request.getParameter("cod");
                    pL.visualizzaModificaPizza(cod);
                    request.setAttribute("paginapizza", pL);
                    forward(request, response, "/PaginaPizza.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            case ("visualizzaPizza"): {                
                    PaginaPizza pL = new PaginaPizza();
                    String cod = (String) request.getParameter("cod");
                    pL.visualizzaPizza(cod);
                    request.setAttribute("paginapizza", pL);
                    forward(request, response, "/PaginaPizza.jsp");                
            }
            break;
            case ("modificaPizza"): {
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    PaginaPizza pL = new PaginaPizza();
                    String cod = request.getParameter("cod");
                    String nome = request.getParameter("nome");               
                    String descrizione = request.getParameter("descrizione");
                    String immagine = request.getParameter("immagine");
                    double prezzo = Double.parseDouble((String)request.getParameter("prezzo"));                   
                    pL.modificaPizza(cod, nome, descrizione, prezzo, immagine);
                    
                    request.setAttribute("paginapizza", pL);
                    forward(request, response, "/PaginaPizza.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
            case ("cancellaPizza"): {
                String permesso = (String) session.getAttribute("permesso");
                if (permesso != null && permesso.equals("admin")) {
                    Amministrazione admin = new Amministrazione();
                    String cod = (String)request.getParameter("cod");
                    if (cod != null && !(cod.equals(""))) {
                        boolean successo = admin.rimuoviPizza(cod);
                        if (successo==true) {
                            request.setAttribute("notifica", "Pizza rimosso dal catalogo");
                        } else {
                            request.setAttribute("notifica", "Impossibile rimuovere Pizza dal catalogo!");
                        }
                    }

                    Catalogo catalogo = (Catalogo) session.getAttribute("catalogo");
                    if (catalogo == null) {
                        catalogo = new Catalogo();
                    }
                    catalogo.search(null, true);
                    session.setAttribute("catalogo", catalogo);
                    request.setAttribute("amministrazione", admin);
                    forward(request, response, "/Catalogo.jsp");
                } else {
                    forward(request, response, "/ErroreLogin.jsp");
                }
            }
            break;
                
             case ("Prenotazione"): {
                String username = (String) session.getAttribute("username");
                Prenotazione pre = (Prenotazione) request.getAttribute("prenotazione");
                if (pre == null) {
                    pre = new Prenotazione();
                }
                if (username == null || username.equals("")) {
                    forward(request, response, "/ErroreLogin.jsp");
                } else {
                    pre.visualizzaPrenotazioni(username);
                    request.setAttribute("prenotazione", pre);
                    request.setAttribute("username", username);
                    forward(request, response, "/Prenotazione.jsp");
                }
             }
            break;
            default:
                forward(request, response, "/ErroreRichiesta.jsp");
        }
    }

}
