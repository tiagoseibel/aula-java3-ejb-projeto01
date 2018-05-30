package web;

import beans.ConsultaCreditoBeanRemote;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import java.math.BigDecimal;
import java.util.stream.Collectors;
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
        
        int client_id = parseInt( request.getParameter("id") );
//        saida.write("O credito do cliente é: " 
//                + bean.getCredito(client_id) );
        
        JsonObject retorno = Json.createObjectBuilder()
                .add("saldo", bean.getCredito(client_id) )
                .build();
       
        String content = "";
        try ( BufferedReader leitor = request.getReader() ) {
            content = leitor.lines().collect( Collectors.joining() );
        }
        
        saida.write( retorno.toString() );
        
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
