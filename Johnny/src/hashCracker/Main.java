package hashCracker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {
        if(supportingFunctions.checkForHelpFlag(args)){
            supportingFunctions.printHelpAndExit();
        }
        if(supportingFunctions.checkForListAlgorithmFlag(args)){
            supportingFunctions.listAlgorithmsAndCloseProgram();
        }
        String[] parsedArgs = supportingFunctions.parseArgs(args);
        Config params = new Config(parsedArgs);
        Consts.setConfig(params);
        long startTime = System.nanoTime();
        SharedState state = new SharedState();
        LinkedBlockingQueue<String> linesQueue = new LinkedBlockingQueue    <String>();
        ProducerThreadRunnable producer = new ProducerThreadRunnable(state, linesQueue);
        ProgressMonitorThreadRunnable progressMonitor = new ProgressMonitorThreadRunnable(state, Consts.getConfig().totalLines);

        ExecutorService executor = Executors.newFixedThreadPool(Consts.getConfig().numOfThreads);
        executor.execute(producer);
        executor.execute(progressMonitor);
        for (int i =0; i<Consts.getConfig().numOfThreads; i++){
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
/*
list of algos:
SHA3-512
SHA-1
SHA-384
SHA3-384
SHA-224
SHA-512/256
SHA-256
MD2
SHA-512/224
SHA3-256
SHA-512
SHA3-224
MD5
 */

}


