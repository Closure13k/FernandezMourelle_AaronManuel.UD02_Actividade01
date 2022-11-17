package Ejercicios;

import Data.PresetData;
import Database.Database;
import Database.DatabaseQueries;
import Models.Client;
import Models.EjercicioException;
import Models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Unha vez creadas as táboas nas bases de datos fai un programa Java que encha
 * as táboas PRODUTOS e CLIENTES (os datos a insertar defínense no propio
 * programa). O programa Java recibe un argumento ao executalo dende a liña de
 * comandos cxo valor é 1 ou 2. Se o valor é 1 debes encher as táboas da base de
 * daos de MySQL e se é 2 debes enchelas na base de datos de SQLite.
 *
 * @author AARONFM
 */
public class ex1 {

    /**
     * Inserción de datos en MySQL y SQLite.
     *
     * Deberá de recibir el número 1 y 2 como argumento.
     *
     * @param args El número a recibir por comando.
     * @throws EjercicioException Si se interrumpe la ejecución normal.
     *
     * La ejecución se puede interrumpir por: 1. Recibir un número de argumentos
     * incorrecto.
     *
     * 2. Recibir un argumento que NO es númerico.
     *
     * 3. Que el argumento numérico no sea el esperado.
     */
    public static void insertData(String[] args) throws EjercicioException {
        int argument = validateArgument(args);
        switch (argument) {
            case 1 -> {
                insertIntoMySQL();
            }
            case 2 -> {
                insertIntoSQLite();
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
        if (args.length != 1) {
            throw new EjercicioException(EjercicioException.illegalArguments(1));
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException nfex) {
            throw new EjercicioException(EjercicioException.ILLEGAL_VALUE);
        }
    }

    /**
     * Inserta la lista de datos en la BD MySQL.
     */
    private static void insertIntoMySQL() {
        try {
            Connection instance = Database.getMySqlInstance();
            //Autocommit a false.
            instance.setAutoCommit(false);
            PreparedStatement prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_CLIENT);
            //Primer lote: Clientes.
            prepareClientBatch(PresetData.generateClients(), prepareInsertion);
            int[] clientBatchResults = prepareInsertion.executeBatch();
            //Segundo lote: Productos.
            prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_PRODUCT);
            prepareProductBatch(PresetData.generateProducts(), prepareInsertion);
            int[] productBatchResults = prepareInsertion.executeBatch();

            prepareInsertion.close();

            //Autocommit a true.
            instance.setAutoCommit(true);
            //Cerramos conexión.
        } catch (SQLException ex) {
            //TODO: HANDLE EXCEPTIONS
        }
    }

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

    private static void prepareProductBatch(List<Product> products, PreparedStatement prepareInsertion) {
        for (Product product : products) {
            
        }
    }

    /**
     * Inserta la lista de datos en la BD SQLite.
     */
    private static void insertIntoSQLite() {
        try {
            Connection instance = Database.getSqliteInstance();
        } catch (SQLException ex) {
            //TODO: HANDLE EXCEPTIONS
        }
    }

}
