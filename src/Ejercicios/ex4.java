package Ejercicios;

import Database.Database;
import static Ejercicios.ex2.validateArgument;
import Models.EjercicioException;
import Models.Sale;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Crea un procedemento almacenado na base de datos ao que se lle pasen como
 * parámetros de entrada o identificador do produto e a cantidade dun produto
 * nunha venta, e actualice o STOCKACTUAL na táboa PRODUTOS.
 *
 * Modifica o código do exercicio 2 para que cando se realice unha venta se
 * actualice o stock.
 *
 * Crea unha función que devolva o STOCKACTUAL dun produto cuxo identificador
 * pasarase como parámetro a dito procedemento.
 *
 * Modifica o código do exercicio 2 para que se realice a comprobación de que
 * existan as unidades dispoñibles en stock para que a venta sexa realizada con
 * éxito.
 *
 * Todas as accións que compoñen unha venta funcionarán como unha unidade de
 * execución e deben ser tratadas como unha transacción.
 *
 * @author AaronFM
 */

/*
   En vez de modificar el ejercicio 2, reharé el ejercicio 2 en este.     
 */
public class ex4 {

    /**
     * Introduce los datos de una venta en la tabla.
     *
     * @param args Los datos a recibir: [BASE DATOS] [ID VENTA] [ID CLIENTE] [ID
     * PRODUCTO] [CANTIDAD]
     * @throws EjercicioException
     */
    public static void insertDataIntoSales(String[] args) throws EjercicioException {
        //Validación de campos importada del ejercicio 2.
        int databaseNumber = validateArgument(args);
        //Validación de los restantes mediante construcción en modelo.
        Sale sale = Sale.createSaleFromArgs(args);
        try {
            pickDatabaseAndInsertSale(databaseNumber, sale);
        } catch (SQLException ex) {
            throw new EjercicioException("%s: %s".formatted(ex.getErrorCode(), ex.getMessage()));
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
    private static void pickDatabaseAndInsertSale(int databaseNumber, Sale sale) throws EjercicioException, SQLException {
        Connection instance;
        switch (databaseNumber) {
            case 1 ->
                instance = Database.getMySqlInstance();

            case 2 ->
                instance = Database.getSqliteInstance();

            default ->
                throw new EjercicioException(EjercicioException.INVALID_VALUE);
        }
        try (instance) {
            insertSale(sale, instance);
        }
    }

    private static void insertSale(Sale sale, Connection instance) {
        //Comprobar stock mediante SELECT y no me complico.
    }
}
