package beans;

import java.util.Date;
import javax.ejb.Remote;

@Remote
public interface ConsultaCreditoBeanRemote {
    public double getCredito(int cliente_id);
    public boolean registrarCompra(
            int id_cliente, String nome_loja,
            Date data_compra, double valor_compra,
            int id_compra
    );    
}
