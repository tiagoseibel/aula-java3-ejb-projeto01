package web;

import beans.ConsultaCreditoBeanRemote;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

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

        // JsonParse (String -> JSON object
        JsonReader reader = Json.createReader(new StringReader(content));
        JsonObject compra = reader.readObject();

        int id_compra = compra.getJsonNumber("id").intValue();
        int id_cliente = compra.getJsonNumber("id_cliente").intValue();
        String nome_loja = compra.getJsonString("nomeLoja").getString();
        String data_compra = compra.getJsonString("dataCompra").getString();
        double valor_compra = compra.getJsonNumber("valorCompra").doubleValue();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = formatter.parse(data_compra);
        } catch (ParseException ex) {
            throw new ServletException(ex);
        }

        boolean ret = bean.registrarCompra(
                id_cliente, nome_loja,
                data, valor_compra, id_compra
        );

        if (ret) {
            saida.write("{ \"msg\": \"OK\" }");
        } else {
            saida.write("{ \"msg\": \"Saldo insuficiente!\" }");
        }

    }
}
