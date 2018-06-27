package beans;

import dao.ClienteDAO;
import dto.CompraDTO;
import exceptions.AppException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import model.Cliente;

@Stateless
public class ConsultaCreditoBean implements ConsultaCreditoBeanRemote, ConsultaCreditoBeanLocal {

    @Override
    public double getCredito(int cliente_id) throws AppException {
        double credito = 0;

        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.findById(cliente_id);

            credito = cliente.getValorCredito();

        } catch (Exception ex) {
            Logger.getLogger(ConsultaCreditoBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new AppException("Houve um erro ao processar a requisição! Contate o suporte!");
        }

        return credito;
    }

    public boolean registrarCompra(CompraDTO compra) throws AppException {
        boolean result = false;
        double credito = getCredito(compra.getCliente().getId());

        if (compra.getValorCompra() > credito) {
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
                p.setInt(1, compra.getId());
                p.setInt(2, compra.getCliente().getId());
                p.setDate(3, new java.sql.Date(compra.getDatacompra().getTime()));
                p.setString(4, compra.getNomeLoja());
                p.setDouble(5, compra.getValorCompra());
                p.execute();

                // Atualiza o saldo
                double novoCredito = credito - compra.getValorCompra();

                // Atualiza cliente
                PreparedStatement p2 = con.prepareStatement(sql2);
                p2.setDouble(1, novoCredito);
                p2.setInt(2, compra.getCliente().getId() );
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
