package ua.nure.mishchenko.practice5;


public class Part1 {
    private static final int COUNTER = 5;
    private static final int INTERVAL = 222;

    public static void run() {
        int counter = 0;
        while (counter < COUNTER) {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }

    private static class RunnableImpl implements Runnable {

        @Override
        public void run() {
            Part1.run();
        }
    }

    private static class ThreadImpl extends Thread {

        @Override
        public void run() {
            Part1.run();
        }
    }

    public static void main(String[] args) {

        Thread thread1 = new Thread(new RunnableImpl());
        Thread thread2 = new ThreadImpl();
        try {
            thread1.start();
            thread1.join();
            thread2.start();
            thread2.join();
            Thread thread = new Thread(Part1::run);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
