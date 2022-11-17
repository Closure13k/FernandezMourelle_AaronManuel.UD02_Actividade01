package Models;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Record de ventas
 */
public record Sale(int id, Date date, int clientId, int productId, int quantity) {

    /**
     * Durante la creación: El id no se usará, por lo tanto será 0.
     *
     * @param args Los argumentos para la creación del objeto.
     * @return nuevo objeto venta para la inserción en la base de datos.
     * @throws EjercicioException
     */
    public static Sale createSaleFromArgs(String[] args) throws EjercicioException {
        try {
            return new Sale(
                    Integer.parseInt(args[1]),
                    Date.valueOf(LocalDate.now()),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4])
            );
        } catch (NumberFormatException nfex) {
            throw new EjercicioException("Error al construir una venta. Los parámetros a recibir han de ser numéricos");
        }
    }
}
