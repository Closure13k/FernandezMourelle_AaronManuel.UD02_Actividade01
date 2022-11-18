package Models;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Record de ventas.
 */
public final record Sale(int id, Date date, int clientId, int productId, int quantity) {

    /**
     * Excepciones específicas de la construcción del objeto.
     */
    private static final String ILLEGAL_VALUE_EXCEPTION = "Error al construir una venta. Todos los parámetros a recibir han de ser numéricos.";
    private static final String INVALID_QUANTITY_EXCEPTION = "Error al preparar la venta: La cantidad de stock no puede ser menor a 0.";

    /**
     * Durante la creación: El id no se usará, por lo tanto será 0.
     *
     * @param args Los argumentos para la creación del objeto.
     * @return nuevo objeto venta para la inserción en la base de datos.
     * @throws EjercicioException
     */
    public static Sale createSaleFromArgs(String[] args) throws EjercicioException {
        try {
            int id = Integer.parseInt(args[1]);
            int clientId = Integer.parseInt(args[2]);
            int productId = Integer.parseInt(args[3]);
            int quantity = Integer.parseInt(args[4]);
            if (quantity < 0) {
                throw new EjercicioException(INVALID_QUANTITY_EXCEPTION);
            }
            return new Sale(id, Date.valueOf(LocalDate.now()), clientId, productId, quantity);
        } catch (NumberFormatException nfex) {
            throw new EjercicioException(ILLEGAL_VALUE_EXCEPTION);
        }
    }
}
