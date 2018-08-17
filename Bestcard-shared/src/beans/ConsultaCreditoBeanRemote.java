package beans;

import dto.CompraDTO;
import exceptions.AppException;
import javax.ejb.Remote;

@Remote
public interface ConsultaCreditoBeanRemote {

    // Interfaces de acesso ao Bean
    
    public double getCredito(int cliente_id) throws AppException;

    public boolean registrarCompra(CompraDTO compra) throws AppException;
}
