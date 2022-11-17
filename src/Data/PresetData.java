/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import Models.Client;
import Models.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AaronFM
 */
public interface PresetData {

    public static List<Client> generateClients() {
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

    public static List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        Product product;
        for (int i = 1; i <= 10; i++) {
            product = new Product(i, "Producto%s".formatted(i), i * 100, i * 10, (i * 10 + (i * 4)));
            products.add(product);
        }
        return products;
    }
}
