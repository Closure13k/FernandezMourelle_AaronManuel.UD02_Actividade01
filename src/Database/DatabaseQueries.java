package Database;

/**
 * Sentencias SQL para gestionar cómodamente.
 */
public interface DatabaseQueries {
    /**
     * Sentencia de adición SQL para la tabla clientes.
     */
    public static final String INSERT_CLIENT = "INSERT INTO CLIENTES (id, nif, nombre, direccion, poblacion, telefono) VALUES(?,?,?,?,?,?)";
    
    /**
     * Sentencia de adición SQL para la tabla productos.
     */
    public static final String INSERT_PRODUCT = "INSERT INTO PRODUCTOS (id, descripcion, stockactual, stockminimo, pvp) VALUES(?,?,?,?,?)";
    /**
     * Sentencia de adición SQL para la tabla ventas.
     */
    public static final String INSERT_SALE = "INSERT INTO VENTAS (IDVENTA, DATAVENTA, IDCLIENTE, IDPRODUCTO, CANTIDAD) VALUES (?,?,?,?,?)";
}