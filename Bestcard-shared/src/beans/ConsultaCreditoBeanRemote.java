package beans;

import javax.ejb.Remote;

@Remote
public interface ConsultaCreditoBeanRemote {
    public double getCredito(int cliente_id);
}
