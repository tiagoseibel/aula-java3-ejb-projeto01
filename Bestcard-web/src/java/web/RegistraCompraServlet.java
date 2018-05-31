package web;

import beans.ConsultaCreditoBeanRemote;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import static java.lang.Double.parseDouble;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;

public class RegistraCompraServlet extends HttpServlet{

    @EJB
    private ConsultaCreditoBeanRemote bean;
    
    @Override
    protected void doPost(HttpServletRequest req, 
            HttpServletResponse resp) 
            throws ServletException, IOException
    {
        resp.setContentType("text/html");
        PrintWriter saida = resp.getWriter();
        
        int id_compra = parseInt( req.getParameter("id_compra") );
        int id_cliente = parseInt( req.getParameter("id_cliente") );
        String data_compra = req.getParameter("data_compra");
        String nome_loja = req.getParameter("nome_loja");
        double valor_compra = 
                parseDouble( req.getParameter("valor_compra") );
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = formatter.parse(data_compra);
        } catch (ParseException ex) {
            throw new ServletException(ex);
        }
        
        boolean ret = bean.registrarCompra (
                        id_cliente, nome_loja, 
                        data, valor_compra, id_compra
                      );
        
        if (ret) {
            saida.write("OK");
        } else { 
            saida.write("Saldo insuficiente para compra!"); 
        }
    }
}
