package ua.nure.mishchenko.practice5;

import static java.lang.Thread.sleep;

public class Part3 {

    private int timeout;
    private int iterations;
    private int runnableCount;

    private int counter;
    private int counter2;

    private Thread[] threads;

    public Part3(int count, int iters, int time) {
        runnableCount = count;
        iterations = iters;
        timeout = time;
        threads = new Thread[runnableCount];
    }

    private void reset() {
        counter = 0;
        counter2 = 0;
    }

    private void execute(Runnable runnable) {
        for (int i = 0; i < runnableCount; i++) {
            threads[i] = new Thread(runnable, String.valueOf(i));
            threads[i].start();
            if (i == runnableCount - 1) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void test() {
        execute(new Unsync());
    }


    private void testSync() {
        execute(new Sync());
    }

    class Unsync implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                try {
                    System.out.printf("%s %s%n", counter, counter2);
                    counter++;
                    sleep(timeout);
                    counter2++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Sync implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                synchronized (this) {
                    System.out.printf("%s %s%n", counter, counter2);
                    try {
                        counter++;
                        sleep(timeout);
                        counter2++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Part3 obj = new Part3(3, 5, 100);
        obj.test();
        obj.reset();
        obj.testSync();
    }
}
