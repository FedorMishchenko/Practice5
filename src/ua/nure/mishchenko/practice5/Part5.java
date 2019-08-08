package ua.nure.mishchenko.practice5;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Part5 extends Thread {
    private static final Object MONITOR = new Object();
    private static final int THREADS_NUMBER = 10;
    private static final int COLUMNS = 20;
    private static final int EOL_LENGTH = System.lineSeparator().length();
    private static final String FILENAME = "part5.txt";
    private final RandomAccessFile raf;
    private final int name;

    public Part5(final int name, final RandomAccessFile raf) {
        this.name = name;
        this.raf = raf;
    }

    public void write(final int pointer) throws IOException {
        synchronized (MONITOR) {
            raf.seek((COLUMNS + EOL_LENGTH) * name + pointer);
            raf.writeBytes(name + System.lineSeparator());
        }
    }

    public static String read(final String fileName) throws IOException {
        StringBuilder result = new StringBuilder();
        try (RandomAccessFile in = new RandomAccessFile(fileName, "r")) {
            int b = in.read();
            while (b != -1) {
                result.append((char) b);
                b = in.read();
            }
        }
        return result.toString().trim();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < COLUMNS; i++) {
                write(i);
                TimeUnit.MILLISECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        if (deleteFile()) {
            Part5[] threads = new Part5[THREADS_NUMBER];
            try (RandomAccessFile fileOut = new RandomAccessFile(FILENAME, "rw")) {
                for (int i = 0; i < threads.length; i++) {
                    threads[i] = new Part5(i, fileOut);
                    threads[i].start();
                }
                for (int i = 0; i < threads.length; i++) {
                    threads[i].join();
                }
                System.out.println(read(FILENAME));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static boolean deleteFile() {
        File file = new File(Part5.FILENAME);

        if (file.exists()) {
            try {
                Files.delete(file.toPath());
                return true;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
