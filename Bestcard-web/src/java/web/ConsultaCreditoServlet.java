package web;

import beans.ConsultaCreditoBeanRemote;
import exceptions.AppException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;

public class ConsultaCreditoServlet extends HttpServlet {

    @EJB
    private ConsultaCreditoBeanRemote bean;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter saida = response.getWriter();
        
        System.out.println("Test = " + request.getRequestURI());
        
        int client_id = parseInt( request.getParameter("id") );
        
        JsonObject retorno = null;
        
        try {
            retorno = Json.createObjectBuilder()
                    .add( "saldo", bean.getCredito(client_id) )
                    .build();
            
        } catch (AppException ex) {
            retorno = Json.createObjectBuilder()
                           .add("message", ex.getMessage())
                           .build();
            response.setStatus(500);
        }

        saida.write( retorno.toString() );
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//                String content = "";
//        try ( BufferedReader leitor = request.getReader() ) {
//            content = leitor.lines().collect( Collectors.joining() );
//        }
        
    }
}
