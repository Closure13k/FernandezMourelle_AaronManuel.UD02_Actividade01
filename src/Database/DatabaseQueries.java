package Database;

/**
 * Sentencias SQL para gestionar cómodamente.
 *
 * @author AARONFM
 */
public interface DatabaseQueries {

    public static final String INSERT_CLIENT = "INSERT INTO CLIENTES (id, nif, nombre, direccion, poblacion, telefono) VALUES(?,?,?,?,?,?)";
    public static final String INSERT_PRODUCT = "";
}
