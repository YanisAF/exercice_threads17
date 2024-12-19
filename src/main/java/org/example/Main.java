package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>();
        inventory.put("Produit A", 50);
        inventory.put("Produit B", 40);
        inventory.put("Produit C", 20);

        Runnable buyTask = () -> {
            for (int i = 0; i < 5; i++) {
                // selectionner un produit au pif
                String productName = "Produit "+(char)('A'+ ThreadLocalRandom.current().nextInt(3));
                // décremente le stock si le produit existe et a un stock positif
                inventory.computeIfPresent(productName, (key, value) -> value - 1);
                System.out.println(Thread.currentThread().getName() + " a acheté 1 unité de " + productName);
            }
        };

        Runnable restockTask = () -> {
            for (int i = 0; i < 10; i++) {
                // selectionner un profuit au pif
                String productName = "Produit "+(char)('A'+ ThreadLocalRandom.current().nextInt(3));
                // restock de 10
                inventory.merge(productName, 10, Integer::sum);
                System.out.println(Thread.currentThread().getName() + " a acheté 1 unité de " + productName);
            }
        };

        // Utilisation d'un executor
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // soummet les taches à mon executor
        executor.submit(buyTask);
        executor.submit(restockTask);

        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        System.out.println("Inventaire final : " + inventory);
    }
}