package Ejercicios;

import Database.Database;
import Database.DatabaseQueries;
import Models.EjercicioException;
import Models.Sale;
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
        int argument = validateArgument(args);
        Sale sale = Sale.createSaleFromArgs(args);
        System.out.println(sale);
        switch (argument) {
            case 1 -> {
                try {
                    getClientInfoFromMySQL(args[1]);
                } catch (SQLException ex) {
                    throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
                }
            }
            case 2 -> {
                try {
                    getClientInfoFromSQLite(args[1]);
                } catch (SQLException ex) {
                    throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
                }
            }
            default ->
                throw new EjercicioException(EjercicioException.INVALID_VALUE);
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
    public static int validateArgument(String[] args) throws EjercicioException {
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

    private static int validateClientId(String arg) throws EjercicioException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException nfex) {
            throw new EjercicioException("%s %s".formatted("Error al reconocer el ID recibido.", EjercicioException.ILLEGAL_VALUE));
        }
    }

    private static void getClientInfoFromMySQL(String arg) throws EjercicioException, SQLException {
        int id = validateClientId(arg);
        try ( Connection instance = Database.getMySqlInstance()) {
            instance.setAutoCommit(false);
            getClientInfo(id, instance);
            instance.setAutoCommit(true);
        }
    }

    private static void getClientInfoFromSQLite(String arg) throws EjercicioException, SQLException {
        int id = validateClientId(arg);
        try ( Connection instance = Database.getSqliteInstance()) {
            instance.setAutoCommit(false);
            getClientInfo(id, instance);
            instance.setAutoCommit(true);
        }
    }

    private static void getClientInfo(int id, Connection instance) throws SQLException {
        String clientStmt = DatabaseQueries.GET_CLIENT_BY_ID;
        String saleStmt = DatabaseQueries.GET_CLIENT_SALES;
        try ( PreparedStatement clientQuery = instance.prepareStatement(clientStmt)) {
            clientQuery.setInt(1, id);
            ResultSet clientResult = clientQuery.executeQuery();
            StringBuilder sb = new StringBuilder();
            if (clientResult.next()) {
                sb.append("Nombre cliente: ").append(clientResult.getString("nombre"));
                try ( PreparedStatement salesQuery = instance.prepareStatement(saleStmt)) {
                    salesQuery.setInt(1, id);
                    ResultSet salesResult = salesQuery.executeQuery();
                    if (salesResult.next()) {
                        do {
                            //TODO: Chain query information.
                        } while (salesResult.next());
                        /*
                        Venta: idventa
                        Data venta: data
                        Produto: descripción do produto
                        Cantidade: cantidade
                        PVP: pvp
                        Importe: cantidade*pvp
                        Número total de ventas: ____
                        Importe total: ___
                         */
                    }
                }
            }
        }
    }

}
