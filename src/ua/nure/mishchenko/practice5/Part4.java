package ua.nure.mishchenko.practice5;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;


public class Part4 {
    private int[][] matrix;
    private static final String FILENAME = "part4.txt";
    private static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        Part4 obj = new Part4();
        obj.matrix = Util.read(FILENAME);
        maxSync(obj.matrix);
        max(obj.matrix);


    }

    private static void max(int[][] matrix) {
        int max = matrix[0][0];
        long start = System.currentTimeMillis();
        for (int[] i : matrix) {
            for (int j : i) {
                try {
                    Thread.sleep(1);
                    max = max > j ? max : j;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        getMaxAndTime(max, start);
    }

    private static void maxSync(int[][] matrix){
        ExecutorService service = Executors.newFixedThreadPool(matrix.length);
        Future[] futures = new Future[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            futures[i] = service.submit(new Find(matrix[i]));
        }
        int max = matrix[0][0];
        long start = System.currentTimeMillis();
        for (int i = 0; i < matrix.length; i++) {
            try {
                max = max > (Integer) futures[i].get()? max : (Integer) futures[i].get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        getMaxAndTime(max, start);
    }

    private static void getMaxAndTime(int max, long start) {
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println(max);
        System.out.println(time);
    }

    private static class Find implements Callable<Integer> {
        private int[] column;

        Find(int[] column) {
            this.column = column.clone();
        }

        @Override
        public Integer call() {
            int max = column[0];
            final int[] arr = column;
            for (int i = 1; i < arr.length; i++) {
                try {
                    Thread.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                max = max > arr[i] ? max : arr[i];
            }
            return max;
        }
    }
    public static boolean isNumeric(String string) {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }
    public static String toString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] i : matrix) {
            for (int j : i) {
                sb.append(String.format("%d ", j));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    static final class Util {
        private Util() {
            throw new UnsupportedOperationException("Utility class");
        }
        public static void createFile(){
            int[][] matrix = fill(4,20);
            write(Part4.toString(matrix), FILENAME);
        }

        public static int[][] fill(int rows, int columns) {
            int[][] matrix = new int[rows][columns];
            SecureRandom number = new SecureRandom();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    matrix[i][j] = number.nextInt(1000);
                }
            }
            return matrix;
        }
        public static int[][] read(String fileName) {
            List<String> list = new ArrayList<>();
            try (BufferedReader reader =
                         new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int[][] arr = new int[list.size()][list.get(0).trim()
                    .replaceAll(System.lineSeparator(),"").split(" ").length];
            for (int i = 0; i < arr.length; i++){
                String[] tmp = list.get(i).split(" ");
                for (int j = 0; j < arr[0].length; j++){
                    if(isNumeric(tmp[j])) {
                        arr[i][j] = Integer.parseInt(tmp[j]);
                    }
                }
            }
            return arr;
        }
        public static void write(String matrix, String path) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path), ENCODING))) {
                writer.write(matrix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
