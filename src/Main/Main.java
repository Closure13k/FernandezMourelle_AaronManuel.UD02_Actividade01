package Main;

import Ejercicios.*;
import Models.EjercicioException;

public class Main {

    public static void main(String[] args) {
        try {
            /*
            Llamar una vez y cuando no haya registros.
            Si salta una sola referencia a foránea, tumbará toda la inserción
            ya que está hecha con batch update.
             */
            

            /*
            Ejercicio 3: [BASE DATOS] [ID CLIENTE]
             */
//            ex3.getClientInformation(new String[]{"1", "1"});

            /*
            Ejercicios 2 y 4:
            Los datos a recibir: [BASE DATOS] [ID VENTA] [ID CLIENTE] [ID PRODUCTO] [CANTIDAD]
             */
            //
//            ex2.insertDataIntoSales(new String[]{"1", "7", "1", "100", "1"});
//            ex4.insertDataIntoSales(new String[]{"1", "10", "1", "1", "1"});
            ex5.getProductInformation("100");
        } catch (EjercicioException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
