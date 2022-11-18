package Database;

import java.sql.SQLException;

public interface DatabaseExceptions {

    /**
     * Error 1062. Clave duplicada.
     */
    public static final String DUPLICATE_KEY = "Ya hay una venta registrada con ese ID.";
    /**
     * Error 1452. Clave foránea.
     */
    public static final String NO_SUCH_ENTRY = "No existe el ID que referencia a la tabla:";

    /**
     * Identifica el código de error y asigna en consecuencia.
     * @param sqlex La excepción a reconocer.
     * @return El mensaje
     */
    public static String identifyErrorCode(SQLException sqlex) {
        String message;
        switch (sqlex.getErrorCode()) {
            case 1062 -> {
                message = DatabaseExceptions.DUPLICATE_KEY;
            }
            case 1452 -> {
                message = getTableFromExceptionInfo(sqlex);
            }
            default -> {
                message = "%s: %s".formatted(sqlex.getErrorCode(), sqlex.getMessage());
            }
        }
        return message;
    }

    /**
     * Experimentando.
     *
     * Recoge la excepción SQL y pregunta por la tabla que lanzó el error de
     * clave foránea.
     *
     * @param sqlex
     * @return
     */
    public static String getTableFromExceptionInfo(SQLException sqlex) {
        String message = sqlex.getMessage().transform(
                string -> string.contains("PRODUCTOS") ? "PRODUCTOS" : "CLIENTES"
        );
        return "%s %s".formatted(NO_SUCH_ENTRY, message);
    }
}
