package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;

@Stateless
public class ConsultaCreditoBean implements ConsultaCreditoBeanRemote, ConsultaCreditoBeanLocal {

    public double getCredito(int cliente_id) {
        double credito = 0;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = 
               DriverManager.getConnection
                 ("jdbc:derby://localhost:1527/bestcard",
                  "sa", "sa");
            
            PreparedStatement p =
                con.prepareStatement
                  ("select * from cliente where id=?");
            p.setInt(1, cliente_id);
            
            ResultSet rs = p.executeQuery();
            if ( rs.next() ) {
                credito = rs.getDouble("valor_credito");
            }

        } catch (ClassNotFoundException ex) {
            System.out.println(""+ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(""+ex.getMessage());
        }
        
        return credito;
    }
}
