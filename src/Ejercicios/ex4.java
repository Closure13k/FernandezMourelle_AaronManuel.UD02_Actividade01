package Ejercicios;

import Database.Database;
import Database.DatabaseQueries;
import Models.EjercicioException;
import Models.Sale;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static Database.DatabaseExceptions.identifyErrorCode;
import static Ejercicios.ex2.validateArgument;
/*
    En vez de modificar el ejercicio 2, reharé el ejercicio 2 en este.
    A mayores: No haré nada sobre SQLite en este ejercicio.
 */
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
        System.out.println("Inserción de %s realizada con éxito.".formatted(sale.formatted()));
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
        //Selección.
        switch (databaseNumber) {
            case 1 ->
                instance = Database.getMySqlInstance();
            default ->
                throw new EjercicioException(EjercicioException.INVALID_VALUE);
        }
        //Apertura, ejecución y cierre.
        try (instance) {
            instance.setAutoCommit(false);
            insertSale(sale, instance);
            instance.setAutoCommit(true);
        }
    }

    /**
     * Comprueba que existe el producto a vender y valida la cantidad vs stock
     * actual.
     *
     * Si todo es correcto, inserta la venta.
     *
     * @param sale La venta.
     * @param instance La entrada a la BD.
     * @throws EjercicioException Si:
     *
     *
     * @throws SQLException Si se produce algún error durante el intento de
     * rollback o commit.
     */
    private static void insertSale(Sale sale, Connection instance) throws EjercicioException, SQLException {
        //Comprobar si existe el producto.
        try ( PreparedStatement productExistsQuery = instance.prepareStatement(DatabaseQueries.COUNT_PRODUCTS)) {
            productExistsQuery.setInt(1, sale.productId());
            try ( ResultSet productExists = productExistsQuery.executeQuery()) {
                productExists.next();
                if (productExists.getInt(1) == 0) {
                    instance.rollback();
                    throw new EjercicioException("El id de producto no existe en la base de datos.");
                }
            }
        }
        //Una vez pasado el primer fail, comprueba el stock.
        int maxStock;
        try ( CallableStatement callQuery = instance.prepareCall(DatabaseQueries.CALL_PRODUCTS_FUNCTION_GET_STOCK)) {
            //Param 1: El valor de return.
            callQuery.registerOutParameter(1, Types.INTEGER);
            //Param 2: El id de producto.
            callQuery.setInt(2, sale.productId());
            callQuery.execute();
            maxStock = callQuery.getInt(1);
        }
        //Si inválido, rollback y salida.
        if (sale.quantity() > maxStock) {
            instance.rollback();
            throw new EjercicioException(
                    "La cantidad en venta (%s) no puede superar el stock actual del producto (%s)."
                            .formatted(sale.quantity(), maxStock)
            );
        }
        //Si válido, registra venta. Podría haber reciclado parte del ejercicio 2, pero pa' qué.
        try ( PreparedStatement prepareInsertion = instance.prepareStatement(DatabaseQueries.INSERT_SALE)) {
            prepareInsertion.setInt(1, sale.id());
            prepareInsertion.setDate(2, sale.date());
            prepareInsertion.setInt(3, sale.clientId());
            prepareInsertion.setInt(4, sale.productId());
            prepareInsertion.setInt(5, sale.quantity());
            prepareInsertion.executeUpdate();
        } catch (SQLException sqlex) {
            instance.rollback();
            throw new EjercicioException(identifyErrorCode(sqlex));
        }
        //Finalmente, llama a procedimiento y realiza commit.
        try ( CallableStatement procedureCall = instance.prepareCall(DatabaseQueries.CALL_PRODUCTS_PROCEDURE)) {
            procedureCall.setInt(1, sale.productId());
            procedureCall.setInt(2, sale.quantity());
            procedureCall.executeUpdate();
            instance.commit();
        }
    }
}
