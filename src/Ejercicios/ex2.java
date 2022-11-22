/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicios;

import Database.Database;
import Database.DatabaseQueries;
import Models.EjercicioException;
import Models.Sale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Database.DatabaseExceptions.identifyErrorCode;

/**
 * Partindo das táboas anteriores, realiza un programa Java para insertar ventas
 * na táboa VENTAS. O programa recibe varios parámetros dende a liña de
 * comandos:
 *
 * 1. O primeiro parámetro indica a base de datos onde se insertará a venta (1
 * ou 2) e o seu significado é como no exercicio anterior.
 *
 * 2. O segundo parámetro indica o identificaor da venta.
 *
 * 3. O terceiro parámetro indica o identificador do cliente.
 *
 * 4. O cuarto parámetro indica a cantidade.
 *
 * Realiza as seguintes comprobacións antes de insertar a venta na táboa:
 *
 * O identificador de venta non debe existir na táboa VENTAS.
 *
 * O identificador de cliente debe existir na táboa CLIENTES.
 *
 * O identificador de produto debe existir na táboa PRODUTOS.
 *
 * A cantidade debe ser maior que 0.
 *
 * A data da venta é a data actual.
 *
 * Unha vez insertada a fila na táboa visualizar unha mensaxe indicándoo. Se non
 * se poido realizar a nserción visualizar o motivo (non existe o cliente, non
 * existe o produto, cantidade menor ou igual a 0, …). Executa o programa e
 * inserta varias ventas nas distintas bases de datos.
 *
 * @author AaronFM
 */
public final class ex2 {

    /**
     * Introduce los datos de una venta en la tabla.
     *
     * @param args Los datos a recibir: [BASE DATOS] [ID VENTA] [ID CLIENTE] [ID
     * PRODUCTO] [CANTIDAD]
     * @throws EjercicioException
     */
    public static void insertDataIntoSales(String[] args) throws EjercicioException {
        int argument = validateArgument(args);
        Sale sale = Sale.createSaleFromArgs(args);
        switch (argument) {
            case 1 -> {
                try {
                    insertSaleIntoMySQL(sale);
                } catch (SQLException ex) {
                    throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
                }
            }
            case 2 -> {
                try {
                    insertSaleIntoSQLite(sale);
                } catch (SQLException ex) {
                    throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
                }
            }
            default ->
                throw new EjercicioException(EjercicioException.INVALID_VALUE);
        }
        System.out.println("Inserción de %s realizada con éxito.".formatted(sale.formatted()));
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
        if (args.length != 5) {
            throw new EjercicioException("%s\n%s".formatted(
                    EjercicioException.illegalArguments(5),
                    "El orden deberá de ser: [BASE DATOS] [ID VENTA] [ID CLIENTE] [ID PRODUCTO] [CANTIDAD]"
            ));
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
    private static void insertSaleIntoMySQL(Sale sale) throws SQLException, EjercicioException {

        try ( Connection instance = Database.getMySqlInstance()) {
            instance.setAutoCommit(false);
            saleInsertion(sale, instance);
            instance.setAutoCommit(true);
        }
    }

    /**
     * Inserta la lista de datos en la BD SQLite.
     */
    private static void insertSaleIntoSQLite(Sale sale) throws SQLException, EjercicioException {
        try ( Connection instance = Database.getSqliteInstance()) {
            instance.setAutoCommit(false);
            saleInsertion(sale, instance);
            instance.setAutoCommit(true);
        }
    }

    /**
     * Inserción SQL en la tabla.
     *
     * @param sale El objeto a insertar.
     * @param instance La conexión abierta (MySQL o SQLite)
     * @throws SQLException Si se produce un error durante la inserción.
     * @throws EjercicioException La excepción reconocida (O un Cod_error :
     * Mensaje si no la reconocí).
     */
    private static void saleInsertion(Sale sale, Connection instance) throws SQLException, EjercicioException {
        try ( PreparedStatement prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_SALE)) {
            prepareInsertion.setInt(1, sale.id());
            prepareInsertion.setDate(2, sale.date());
            prepareInsertion.setInt(3, sale.clientId());
            prepareInsertion.setInt(4, sale.productId());
            prepareInsertion.setInt(5, sale.quantity());
            prepareInsertion.executeUpdate();
            instance.commit();
        } catch (SQLException sqlex) {
            instance.rollback();
            throw new EjercicioException(identifyErrorCode(sqlex));
        }

    }

}
