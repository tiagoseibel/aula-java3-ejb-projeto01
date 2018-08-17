package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ClienteDTO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrarClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tipo de conteúdo da resposta
        response.setContentType("application/json;charset=UTF-8");

        // Leitura do conteudo da requisicao POST
        String content;
        try (BufferedReader leitor = request.getReader()) {
            content = leitor.lines().collect(Collectors.joining());
        }

        // ObjectMapper do Jackson configurado para o formato de data
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        mapper.setDateFormat(formatter);

        // Converte o JSON recebido para um objeto
        ClienteDTO cliente = mapper.readValue(content, ClienteDTO.class);

        // Array de strings a partir do conteudo recebido
        String imagemB64[] = cliente.getUpload().split(",");

        // Converte a imagem em String base64 para byte[]
        byte[] image = Base64.getDecoder().decode( imagemB64[1] );

        // Grava a imagem em uma pasta
        // O byte[] poderia ser enviado ao banco de dados como qualquer variavel
        File file = new File("c:\\temp\\teste.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(image);
        fos.close();

    }
}
