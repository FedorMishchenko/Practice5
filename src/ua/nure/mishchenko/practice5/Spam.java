package ua.nure.mishchenko.practice5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Spam extends Thread {
    private final String[] message;
    private final long[] time;
    private final int lengthCommand;

    public Spam(String[] message, long[] time) {
        this.message = Arrays.copyOf(message, message.length);
        this.time = Arrays.copyOf(time, time.length);
        lengthCommand = Math.min(message.length, time.length);
    }

    public static void main(String[] args) {

        Spam.execute(new String[]{"@@@", "BBBBB"}, new long[]{222, 333});
    }

    @Override
    public void run() {
        int i = 0;
        while (!isInterrupted()) {
            try {
                System.out.println(message[i % (lengthCommand)]);
                Thread.sleep(time[i % (lengthCommand)]);
                i++;
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }


    private static void execute(String[] str, long[] timeout)  {
        Thread t = new Thread(() -> {
            Spam spam = new Spam(str, timeout);
            spam.start();
            byte[] buffer = new byte[10];
            int count;
            try {
                do {
                    while ((count = System.in.read(buffer)) == -1) {
                        System.out.print("");
                    }
                } while (!System.lineSeparator().equals(
                        new String(buffer, 0, count, StandardCharsets.UTF_8)));
                spam.interrupt();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
