package hashCracker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        SharedState state = new SharedState();
        LinkedBlockingQueue<String> linesQueue = new LinkedBlockingQueue<String>();
        ProducerThreadRunnable producer = new ProducerThreadRunnable(state, linesQueue);


        ExecutorService executor = Executors.newFixedThreadPool(Consts.numOfThreads);
        executor.execute(producer);

        for (int i =0; i<Consts.numOfThreads; i++){
            ConsumerThreadRunnable consumer = new ConsumerThreadRunnable(state, linesQueue);
            executor.execute(consumer);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupt flag
            System.err.println("Main thread interrupted while waiting for shutdown.");
        }
        long endTime = System.nanoTime();
        double timeTaken = (double) (endTime - startTime) /1000000000;
        System.out.println("Time Taken: " + timeTaken + "seconds.");


    }
}


