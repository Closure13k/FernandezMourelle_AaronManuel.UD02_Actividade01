package Database;

import java.sql.*;

/**
 *
 * @author AARONFM
 */
public final class Database {

    /**
     * Constantes bases de datos.
     */
    //<editor-fold defaultstate="collapsed" desc="SQLite">
    /**
     * Dirección SQLite.
     */
    private static final String SQLITE_DB = "jdbc:sqlite:.\\sqlite\\unidade2.db";
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MySQL">
    /**
     * Dirección MySQL, usuario y contraseña.
     */
    private static final String MYSQL_DB = "jdbc:mysql://192.168.56.104:3306/unidade2";
    private static final String USERNAME = "usuario";
    private static final String PASSWORD = "abc123.";
//</editor-fold>
    /**
     * Patrón singleton: Sólo una instancia de esta clase.
     */
    private static Connection connection;

    //Constructor a privado para prohibir instanciar un objeto.
    private Database() {
    }

    /**
     * Asigna la conexión a sqlite y la devuelve.
     *
     * @return SQLite Connection
     * @throws SQLException
     */
    public static Connection getSqliteInstance() throws SQLException {
        connection = DriverManager.getConnection(SQLITE_DB);
        return connection;
    }

    /**
     * Asigna la conexión a MySQL y la devuelve.
     *
     * @return MySQL Connection
     * @throws SQLException
     */
    public static Connection getMySqlInstance() throws SQLException {
        connection = DriverManager.getConnection(MYSQL_DB, USERNAME, PASSWORD);
        return connection;
    }

}
