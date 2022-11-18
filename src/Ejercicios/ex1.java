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

/**
 * Unha vez creadas as táboas nas bases de datos fai un programa Java que encha
 * as táboas PRODUTOS e CLIENTES (os datos a insertar defínense no propio
 * programa). O programa Java recibe un argumento ao executalo dende a liña de
 * comandos cxo valor é 1 ou 2. Se o valor é 1 debes encher as táboas da base de
 * daos de MySQL e se é 2 debes enchelas na base de datos de SQLite.
 *
 * @author AARONFM
 */
public final class ex1 {

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
                try {
                    insertIntoMySQL();
                } catch (SQLException ex) {
                    throw new EjercicioException("Error durante la inserción MySQL:\n%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
                }
            }
            case 2 -> {
                try {
                    insertIntoSQLite();
                } catch (SQLException ex) {
                    throw new EjercicioException("Error durante la inserción SQLite:\n%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
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
     *
     * @throws SQLException Si el intento de conexión falla.
     */
    private static void insertIntoMySQL() throws SQLException {
        try ( Connection instance = Database.getMySqlInstance()) {
            instance.setAutoCommit(false);
            dataInsertion(instance);
            instance.setAutoCommit(true);
        }
    }

    /**
     * Inserta la lista de datos en la BD SQLite.
     */
    private static void insertIntoSQLite() throws SQLException {
        try ( Connection instance = Database.getSqliteInstance()) {
            //Bloqueo de autocommit.
            instance.setAutoCommit(false);
            //Gestión de la inserción.
            //Realiza commit o rollback dentro de este método.
            dataInsertion(instance);
            //Habilitado de nuevo.
            instance.setAutoCommit(true);
        }
    }

    /**
     * Inserción de datos. Método compartido por ambas llamadas.
     *
     * @param instance la conexión especificada (MySQL o SQLite).
     * @throws SQLException Si ocurriese un fallo durante el rollback o el
     * setAutoCommit.
     */
    public static void dataInsertion(final Connection instance) throws SQLException {
        try {
            //Primer lote: Clientes.
            PreparedStatement prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_CLIENT);
            Client.prepareClientBatch(PresetData.generateClients(), prepareInsertion);
            prepareInsertion.executeBatch();
            prepareInsertion.close();

            //Segundo lote: Productos.
            prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_PRODUCT);
            Product.prepareProductBatch(PresetData.generateProducts(), prepareInsertion);
            prepareInsertion.executeBatch();
            prepareInsertion.close();
            instance.commit();
        } catch (SQLException ex) {
            instance.rollback();
            ex.printStackTrace();
        }
    }

}
