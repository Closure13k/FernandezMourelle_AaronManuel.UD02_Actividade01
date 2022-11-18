package Models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Record de clientes.
 */
public final record Client(int id, String nif, String name, String address, String town, String phoneNumber) {

    /**
     * Recibe un preparedStatement y una lista de clientes.
     * 
     * Prepara el lote a añadir en la base de datos.
     * @param products Lista de productos a recorrer.
     * @param prepareInsertion El preparedStatement a cargar. Véase {@link Database.DatabaseQueries#INSERT_CLIENT}
     * @throws SQLException Si se produce un error durante la asignación y adición al lote.
     */
    public static void prepareClientBatch(List<Client> clients, PreparedStatement prepareInsertion) throws SQLException {
        for (Client client : clients) {
            prepareInsertion.setInt(1, client.id());
            prepareInsertion.setString(2, client.nif());
            prepareInsertion.setString(3, client.name());
            prepareInsertion.setString(4, client.address());
            prepareInsertion.setString(5, client.town());
            prepareInsertion.setString(6, client.phoneNumber());
            prepareInsertion.addBatch();
        }
    }
}
