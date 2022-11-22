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
    /**
     * Sentencia de consulta SQL para clientes. Recoge el cliente en base al id.
     */
    public static final String GET_CLIENT_BY_ID = "SELECT id, nif, nombre, direccion, poblacion, telefono FROM CLIENTES WHERE id = ?";
    /**
     * Sentencia de consulta SQL para clientes. Recoge el producto en base al
     * id.
     */
    public static String GET_PRODUCT_BY_ID = "SELECT ID, DESCRIPCION, STOCKACTUAL, STOCKMINIMO, PVP FROM PRODUCTOS WHERE ID = ?";
    /**
     * Sentencia de consulta SQL para ventas. Recoge la información del
     * ejercicio 3 en base al id del cliente.
     */
    public static final String GET_CLIENT_SALES = "SELECT IDVENTA, DATAVENTA, DESCRIPCION, CANTIDAD, PVP FROM VENTAS INNER JOIN PRODUCTOS ON (VENTAS.IDPRODUCTO = PRODUCTOS.ID) WHERE IDCLIENTE = ?";
    /**
     * Comprueba si existe el producto.
     */
    public static final String COUNT_PRODUCTS = "SELECT COUNT(ID) FROM PRODUCTOS WHERE ID = ?";
    /**
     * Función de consulta SQL para productos. Recoge el stock actual del
     * producto.
     */
    public static final String CALL_PRODUCTS_FUNCTION_GET_STOCK = "{? = CALL getStockProducto(?)}";
    /**
     * Llama al procedimiento de productos.
     */
    public static String CALL_PRODUCTS_PROCEDURE = "{CALL ACTUALIZAR_STOCK(?,?)}";

}
