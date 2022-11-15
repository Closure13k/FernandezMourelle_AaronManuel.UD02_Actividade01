package Ejercicios;

import Database.Database;
import Models.Client;
import Models.EjercicioException;
import Models.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private static List<Client> generateClients() {
        List<Client> clients = new ArrayList<>();
        Client client;
        for (int i = 1; i <= 10; i++) {
            client = new Client(i, "%s%s3456789A".formatted(i, i + 5),
                    "Cliente%s".formatted(i), "Chantado %s, Bj".formatted(i),
                    "Narón", "981440%s%s%s".formatted(i, i + 1, i + 2));
            clients.add(client);
        }
        return clients;
    }

    private static List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            //TODO: FILL THIS

        }
        return products;
    }

    /**
     * Inserta la lista de datos en la BD MySQL.
     */
    private static void insertIntoMySQL() {
        try {
            List<Client> clients = generateClients();
            List<Product> products = generateProducts();
            Connection instance = Database.getMySqlInstance();
            instance.setAutoCommit(false);
            //TODO: Continue here.
        } catch (SQLException ex) {
            //TODO: HANDLE EXCEPTIONS
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
