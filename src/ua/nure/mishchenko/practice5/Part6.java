package ua.nure.mishchenko.practice5;

public class Part6 {
    private static final Object M = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread = getBlocked();
        System.out.println(thread.getState());
        thread.interrupt();
        thread.join();

        Thread t = new Thread(Part6::waiting);
        t.start();
        while (true) {
            if (t.getState() != Thread.State.RUNNABLE) {
                break;
            }
        }
        synchronized (M) {
            System.out.println(t.getState());
            M.notifyAll();
        }
        System.out.println(thread.getState());
    }


    private static void waiting() {
        synchronized (M) {
            try {
                M.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static Thread getBlocked() {
        final Object monitor = new Object();
        Thread thread = new Thread(() -> {
            synchronized (monitor) {
                /*NOP*/
            }
        });

        new Thread(() -> {
            synchronized (monitor) {
                thread.start();
                while (true) {
                    if (thread.isInterrupted()){
                        break;
                    }
                }
            }
        }).start();

        while (true) {
            if (thread.getState() == Thread.State.BLOCKED) {
                break;
            }
        }

        return thread;
    }
}
