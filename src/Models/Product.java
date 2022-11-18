package Models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Record de productos.
 */
public final record Product(int id, String description, int maxStock, int minStock, int retailPrice) {

    /**
     * Recibe un preparedStatement y una lista de productos.
     * 
     * Prepara el lote a añadir en la base de datos.
     * @param products Lista de productos a recorrer.
     * @param prepareInsertion El preparedStatement a cargar. Véase {@link Database.DatabaseQueries#INSERT_PRODUCT}
     * @throws SQLException Si se produce un error durante la asignación y adición al lote.
     */
    public static void prepareProductBatch(List<Product> products, PreparedStatement prepareInsertion) throws SQLException {
        
        for (Product product : products) {
            prepareInsertion.setInt(1, product.id());
            prepareInsertion.setString(2, product.description());
            prepareInsertion.setInt(3, product.maxStock());
            prepareInsertion.setInt(4, product.minStock());
            prepareInsertion.setInt(5, product.retailPrice());
            prepareInsertion.addBatch();
        }
    }
}
