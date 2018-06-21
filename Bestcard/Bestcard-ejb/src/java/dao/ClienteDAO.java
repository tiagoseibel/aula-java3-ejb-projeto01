package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Cliente;
import util.ConnectionUtil;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO() throws Exception {
        connection = ConnectionUtil.getConnection();
    }

    public Cliente findById(int id) throws Exception {
        try {
            Cliente cliente = new Cliente();
            PreparedStatement p = connection.prepareStatement("select * from cliente where id=?");
            p.setInt(1, id);

            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setValorCredito(rs.getDouble("valor_credito"));
            }
            
            connection.close();
            
            return cliente;
        } catch (SQLException ex) {
            throw new Exception("Erro ao processar consulta! Verifique o log do aplicativo.", ex);
        }
    }
}
