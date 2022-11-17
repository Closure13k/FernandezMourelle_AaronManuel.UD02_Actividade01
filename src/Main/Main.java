package Main;

import Ejercicios.ex1;
import Ejercicios.ex2;
import Models.EjercicioException;

public class Main {

    public static void main(String[] args) {
        try {
            /*
            Llamar una vez y cuando no haya registros.
            Si salta una sola referencia a foránea, tumbará toda la inserción
            ya que está hecha con batch updates.
             */
//            ex1.insertData(new String[]{"2"});
            ex2.insertDataIntoSales(new String[]{"2", "1","1","1","1"});
            
        } catch (EjercicioException ex) {
            ex.printStackTrace();
        }
    }
}
