package Ejercicios;

import Database.Database;
import Database.DatabaseQueries;
import Models.EjercicioException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Preténdese realizar un listado das ventas dun cliente.
 *
 * O programa recibe dous argumentos dende a liña de comandos , o primeiro
 * indica a base de datos da que se consultarán as ventas e o segundo o
 * identificador do cliente cuxas ventas vanse consultar. O programa debe
 * visualizar a seguinte información:
 *
 * Ventas do cliente: nome do cliente
 *
 * Venta: idventa, Data venta: data
 *
 * Produto: descripcón do produto
 *
 * Cantidade: cantidade PVP: pvp
 *
 * Importe: cantidade*pvp
 *
 * Venta: idventa, Data venta: data
 *
 * Produto: descripcón do produto
 *
 * Cantidade: cantidade PVP: pvp
 *
 * Importe: cantidade*pvp
 *
 * Número total de ventas: ____ Importe total: ___
 *
 * @author AARONFM
 */
public class ex3 {

    public static void getClientInformation(String[] args) throws EjercicioException {
        int databaseNumber = validateArgumentsReceived(args);
        int clientId = validateClientId(args[1]);
        try {
            pickDatabaseAndGetClient(databaseNumber, clientId);
        } catch (SQLException ex) {
            throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
        }
    }

    /**
     * Valida el valor introducido y lo traduce al tipo esperado.
     *
     * Dado que recibirá enteros, el valor se traduce a int.
     *
     * @param args Los argumentos por consola.
     * @return El valor traducido a entero para su uso.
     * @throws EjercicioException Si el número de argumentos no es correcto o el
     * argumento recibido no es un número.
     */
    public static int validateArgumentsReceived(String[] args) throws EjercicioException {
        if (args.length != 2) {
            throw new EjercicioException("%s\n%s".formatted(
                    EjercicioException.illegalArguments(2),
                    "El orden deberá de ser: [BASE DATOS] [ID CLIENTE]"
            ));
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException nfex) {
            throw new EjercicioException(EjercicioException.ILLEGAL_VALUE);
        }
    }

    /**
     * Valida el id del cliente.
     *
     * @param candidateId el segundo argumento (id).
     * @return el id en formato integer para su uso.
     * @throws EjercicioException Si el argumento no es un número o el número es
     * menor a 1.
     */
    private static int validateClientId(String candidateId) throws EjercicioException {
        try {
            int id = Integer.parseInt(candidateId);
            if (id < 1) {
                throw new EjercicioException("%s %s".formatted("Error al reconocer el ID recibido.", "El id no puede ser menor a 1."));
            }
            return id;
        } catch (NumberFormatException nfex) {
            throw new EjercicioException("%s %s".formatted("Error al reconocer el ID recibido.", EjercicioException.ILLEGAL_VALUE));
        }
    }

    /**
     * Valida el número de la base de datos (1 o 2) y asigna la conexión
     * adecuada. Tras esta validación, llama al método de consultas.
     *
     * @param clientId El id a buscar, una vez validada la base de datos.
     * @param dataBaseNumber El número de base de datos a validar.
     * @throws EjercicioException
     * @throws SQLException
     */
    private static void pickDatabaseAndGetClient(int dataBaseNumber, int clientId) throws EjercicioException, SQLException {
        Connection instance;
        switch (dataBaseNumber) {
            case 1 -> {
                instance = Database.getMySqlInstance();
            }
            case 2 -> {
                instance = Database.getSqliteInstance();
            }
            default ->
                throw new EjercicioException(EjercicioException.INVALID_VALUE);
        }
        try (instance) {
            getClientInfo(clientId, instance);
        }
    }

    /**
     * Recoge el cliente y las ventas del mismo de la base de datos.
     *
     * @param id Id del cliente en la BD.
     * @param instance La conexión.
     * @throws SQLException Si hubiese un fallo durante la ejecución o lectura
     * de sentencias SQL.
     */
    private static void getClientInfo(int id, Connection instance) throws SQLException {
        String clientStmt = DatabaseQueries.GET_CLIENT_BY_ID;
        String saleStmt = DatabaseQueries.GET_CLIENT_SALES;
        try ( PreparedStatement clientQuery = instance.prepareStatement(clientStmt)) {
            StringBuilder sb = new StringBuilder();
            clientQuery.setInt(1, id);
            ResultSet clientResult = clientQuery.executeQuery();
            //Si existe, construye al cliente.
            //Si no existe, avisa mostrando el ID introducido.
            if (clientResult.next()) {
                sb.append("------------------------------------------------");
                sb.append("\nNombre cliente: ").append(clientResult.getString("nombre"));
                //Iniciamos consulta de ventas.
                try ( PreparedStatement salesQuery = instance.prepareStatement(saleStmt)) {
                    salesQuery.setInt(1, id);
                    ResultSet salesResult = salesQuery.executeQuery();
                    //Si hay resultados construye información, else aviso que no tiene ventas.
                    sb.append(salesResult.next() ? buildSalesInformation(salesResult) : "\nNo hay datos de ventas.");
                }
            } else {
                sb.append("No existe el cliente con id: ").append(id);
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * Construye la información de ventas del cliente.
     *
     * @param salesResult ResultSet que se lee.
     * @return Cadena construida.
     * @throws SQLException Si durante la lectura del ResultSet hay un error.
     */
    public static String buildSalesInformation(ResultSet salesResult) throws SQLException {
        int countSales = 0;
        int totalCost = 0;
        int quantity;
        int pvp;
        StringBuilder sb = new StringBuilder();
        do {
            sb.append("\n------------------------------------------------");
            //Venta: idventa
            sb.append("\n\tVenta: ").append(salesResult.getInt("IDVENTA"));
            //Data venta: data
            sb.append("\n\tData venta: ").append(salesResult.getDate("DATAVENTA"));
            //Produto: descripción do produto
            sb.append("\n\tDescripción: ").append(salesResult.getString("DESCRIPCION"));
            //Cantidade: cantidade
            quantity = salesResult.getInt("CANTIDAD");
            sb.append("\n\tCantidad: ").append(quantity);
            //PVP: pvp
            pvp = salesResult.getInt("PVP");
            sb.append("\n\tPVP: ").append(pvp).append(" €");
            //Importe: cantidade * pvp
            sb.append("\n\tImporte: ").append((quantity * pvp)).append(" €");
            countSales++;
            totalCost += (quantity * pvp);
        } while (salesResult.next());
        sb.append("\n------------------------------------------------");
        //Número total de ventas: ____
        sb.append("\n\tTotal de ventas: ").append(countSales);
        //Importe total: ___
        sb.append("\n\tTotal: ").append(totalCost).append(" €");

        return sb.toString();
    }

}
