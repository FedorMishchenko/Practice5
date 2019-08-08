package ua.nure.mishchenko.practice5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Part2 {
    private static final long TIMEOUT = 3000;
    private static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        execute();
    }

    private static void execute() {
        try(ByteArrayInputStream in = new ByteArrayInputStream(
                System.lineSeparator().getBytes(ENCODING))) {

            long count = in.skip(System.lineSeparator().length());
            if (count > 0) {
                System.setIn(in);
            }
            Thread t = new Thread(() -> Spam.main(null));
            t.join();
            t.start();
            TimeUnit.MILLISECONDS.sleep(TIMEOUT);
            in.reset();
            TimeUnit.MILLISECONDS.sleep(TIMEOUT);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            System.setIn(System.in);
        }
    }
}
