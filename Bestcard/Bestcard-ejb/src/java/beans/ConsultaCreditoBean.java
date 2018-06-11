package beans;

import dao.ClienteDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import model.Cliente;

@Stateless
public class ConsultaCreditoBean implements ConsultaCreditoBeanRemote, ConsultaCreditoBeanLocal {

    public double getCredito(int cliente_id) {
        double credito = 0;

        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.findById(cliente_id);

            credito = cliente.getValorCredito();

        } catch (Exception ex) {
            Logger.getLogger(ConsultaCreditoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return credito;
    }

    public boolean registrarCompra(
            int id_cliente, String nome_loja,
            Date data_compra, double valor_compra,
            int id_compra
    ) {
        boolean result = false;
        double credito = getCredito(id_cliente);

        if (valor_compra > credito) {
            result = false;
        } else {

            String sql = "insert into compras (id_compra, "
                    + "id_cliente, data_compra, nome_loja, "
                    + "valor_compra) values (?, ?, ?, ?, ?)";

            String sql2 = "update cliente set valor_credito = ? "
                    + "where id = ?";

            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                Connection con
                        = DriverManager.getConnection("jdbc:derby://localhost:1527/bestcard",
                                "sa", "sa");

                PreparedStatement p = con.prepareStatement(sql);
                p.setInt(1, id_compra);
                p.setInt(2, id_cliente);
                p.setDate(3, new java.sql.Date(data_compra.getTime()));
                p.setString(4, nome_loja);
                p.setDouble(5, valor_compra);
                p.execute();

                // Atualiza o saldo
                double novoCredito = credito - valor_compra;

                // Atualiza cliente
                PreparedStatement p2 = con.prepareStatement(sql2);
                p2.setDouble(1, novoCredito);
                p2.setInt(2, id_cliente);
                p2.execute();

                result = true;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConsultaCreditoBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ConsultaCreditoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

}
