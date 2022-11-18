/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 * Gestión de errores customizada. Guardo también mensajes constantes para
 * recoger de SQLException.
 *
 * @author AARONFM
 */
public class EjercicioException extends Exception {

    /**
     * Constantes
     */
    public static final String ILLEGAL_ARGS = "El número de argumentos no es el adecuado.";
    public static final String ILLEGAL_VALUE = "El argumento recibido tiene que ser un número.";
    public static final String INVALID_VALUE = "Las opciones son:\n1 - Acceso a MySQL.\n2 - Acceso a SQLite.";

    /**
     * Constructor.
     *
     * @param message El mensaje a mostrar/añadir contexto.
     */
    public EjercicioException(String message) {
        super(message);
    }

    public static String illegalArguments(int argNumber) {
        return "%s El número correcto de argumentos es: %d".formatted(ILLEGAL_ARGS, argNumber);
    }
}
