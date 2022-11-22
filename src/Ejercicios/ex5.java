package Ejercicios;

import Database.DatabaseExceptions;
import Database.DatabaseQueries;
import Models.EjercicioException;
import Models.Product;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Preténdese realizar a consulta dos datos dun produto. Pedirase ao usuario o
 * identificador do produto e amosaranse os seus datos. Se o stock actual está
 * por debaixo do stock mínimo emitirase unha mensaxe indicándoo.
 *
 * @author AaronFM
 */
public class ex5 {

    /**
     * Recoge la información de un producto en base a su id.
     *
     * @param productId
     * @throws EjercicioException Si falla la validación, la lectura o no se
     * encuentra.
     */
    public static void getProductInformation(String productId) throws EjercicioException {
        try {
            int id = Integer.parseInt(productId);
            if (id <= 0) {
                throw new EjercicioException("El id ha de ser mayor a 0.");
            }
            Product product = getProduct(id)
                    .orElseThrow(() -> new EjercicioException("No se encontró el producto con id %s.".formatted(id)));

            System.out.println(product.formatted());
        } catch (NumberFormatException nfex) {
            throw new EjercicioException(EjercicioException.ILLEGAL_VALUE);
        }

    }

    /**
     * Realiza la consulta a la base de datos.
     *
     * @param id
     * @return Optional Si encuentra o no el producto.
     * @throws EjercicioException
     */
    private static Optional<Product> getProduct(int id) throws EjercicioException {
        try ( Connection instance = Database.Database.getMySqlInstance();  PreparedStatement prepareQuery = instance.prepareStatement(DatabaseQueries.GET_PRODUCT_BY_ID)) {
            prepareQuery.setInt(1, id);
            try ( ResultSet queryResult = prepareQuery.executeQuery()) {
                Product product = null;
                if (queryResult.next()) {
                    product = Product.generateFromResultSet(queryResult);
                }
                return Optional.ofNullable(product);
            }
        } catch (SQLException sqlex) {
            throw new EjercicioException(DatabaseExceptions.identifyErrorCode(sqlex));
        }

    }
}
