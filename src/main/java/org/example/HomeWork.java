package org.example;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out ) {
        Treap treap = new Treap();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String[] firstLine = reader.readLine().split(" ");

        int Q = Integer.parseInt(firstLine[1]);

        String[] sequence = reader.readLine().split(" ");

        for(String value: sequence) {
            treap.add(Integer.parseInt(value));
        }

        for(int i = 0; i < Q; i++) {
            String[] operation = reader.readLine().split(" ");
            int firstInt = Integer.parseInt(operation[1]);
            int secondInt = Integer.parseInt(operation[2]);
            switch (operation[0]) {
                case "1": {
                    treap.setAll(firstInt - 1, secondInt, Integer.parseInt(operation[3]));
                    break;
                } case "2": {
                    treap.addToAll(firstInt - 1, secondInt, Integer.parseInt(operation[3]));
                    break;
                } case "3": {
                    treap.add(firstInt - 1, secondInt);
                    break;
                } case "4": {
                    out.write(String.valueOf(treap.getStats(firstInt - 1, secondInt)).getBytes());
                    out.write("\n".getBytes());
                    break;
                }
            }
        }


    }



}
