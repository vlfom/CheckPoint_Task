package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.*;

public class AsynchronousCalculator {

    private static long res[];
    private static MyRunnable threads[];

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "Select the way to input information:\n" +
                        "1. File input\n" +
                        "2. Console input"
        );
        int choice;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Wrong input format.") ;
            return ;
        }
        switch (choice) {
            case 1:
                processInputFromFile();
                break;
            case 2:
                processInputFromConsole();
                break;
            default:
                ;
        }
    }

    public static void processInputFromFile() {
        Scanner input;
        try {
            input = new Scanner(
                    new File(
                            System.getProperty("user.dir") + "\\assets\\input.txt"
                    )
            );
        } catch (FileNotFoundException e) {
            System.out.println("File not found.") ;
            return;
        }
        int n, t;
        try {
            n = input.nextInt();
            t = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input.");
            return;
        }
        if (calculateByExecutors(n, t))
            System.out.println("Successfully calculated sum.");
        else
            System.out.println("Error while calculating.");
    }

    public static void processInputFromConsole() {
        int n, t ;
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter n: ");
            n = sc.nextInt();
            System.out.print("Enter number of threads: ");
            t = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input.");
            return;
        }
        if (calculateByThreadPool(n, t))
            System.out.println("Successfully calculated sum.");
        else
            System.out.println("Error while calculating.");
    }

    synchronized public static void notifyThreadFinished(int threadIndex, long value) {
        res[threadIndex] = value;
    }

    public static boolean calculateByThreadPool(int n, int threadNum) {
        res = new long[threadNum];
        threads = new MyRunnable[threadNum];
        for (int i = 0; i < threadNum; i++)
            threads[i] = new MyRunnable(i, n);
        for (int i = 0; i < threadNum; i++)
            try {
                threads[i].thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        for (int i = 1; i < threadNum; i++)
            if (res[i] != res[0])
                return false;
        return true;
    }

    public static boolean calculateByExecutors(int n, int threadNum) {
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        Future<?> future[] = new Future[threadNum];
        for (int i = 0; i < threadNum; i++) {
            future[i] = service.submit(new MyCallable<Long>(n));
        }
        for (int i = 1; i < threadNum; i++)
            try {
                if (!future[i].get().equals(future[i - 1].get())) {
                    service.shutdown();
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        service.shutdown();
        return true;
    }

    private static class MyRunnable implements Runnable {
        Thread thread;
        int threadIndex;
        int n;

        public MyRunnable(int threadIndex, int n) {
            this.threadIndex = threadIndex;
            this.n = n;
            this.thread = new Thread(this);
            this.thread.start();
        }

        @Override
        public void run() {
            long res = 0;
            for (int i = 0; i <= n; i++)
                res += 2 << (i - (i % 2 == 0 ? 1 : -1));
            AsynchronousCalculator.notifyThreadFinished(threadIndex, res);
        }
    }

    private static class MyCallable<V> implements Callable<V> {
        int n;

        public MyCallable(int n) {
            this.n = n;
        }

        @Override
        public V call() throws Exception {
            Long res = 0L;
            for (int i = 0; i <= n; i++) {
                res += 2 << (i - (i % 2 == 0 ? 1 : -1));
            }
            return (V) res;
        }
    }
}