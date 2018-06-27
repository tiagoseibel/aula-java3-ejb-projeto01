package web;

import beans.ConsultaCreditoBeanRemote;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CompraDTO;
import exceptions.AppException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;

public class RegistraCompraServlet extends HttpServlet {

    @EJB
    private ConsultaCreditoBeanRemote bean;

    @Override
    protected void doPost(HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter saida = resp.getWriter();

        String content = "";
        try (BufferedReader leitor = req.getReader()) {
            content = leitor.lines().collect(Collectors.joining());
        }

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("JSON => " + content);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        mapper.setDateFormat(formatter);
        CompraDTO compra = mapper.readValue(content, CompraDTO.class);
        
        // JsonParse (String -> JSON object
        /*
        JsonReader reader = Json.createReader(new StringReader(content));
        JsonObject compra = reader.readObject();

        int id_compra = compra.getJsonNumber("id").intValue();
        int id_cliente = compra.getJsonNumber("id_cliente").intValue();
        String nome_loja = compra.getJsonString("nome_loja").getString();
        String data_compra = compra.getJsonString("data_compra").getString();
        double valor_compra = compra.getJsonNumber("valor_compra").doubleValue();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = formatter.parse(data_compra);
        } catch (ParseException ex) {
            throw new ServletException(ex);
        }
        */
        boolean ret = false;
        String retorno = "";
        try {
            System.out.println("Loja: " + compra.getNomeLoja());
            ret = bean.registrarCompra(compra);

            if (ret) {
                retorno = "Compra efetuada!";
            } else {
                retorno = "Saldo Insuficiente!";
            }
        } catch (AppException ex) {
            retorno = ex.getMessage();
        }

        JsonObject json = Json.createObjectBuilder()
                .add("message", retorno)
                .build();

        saida.write(json.toString());

    }
}
