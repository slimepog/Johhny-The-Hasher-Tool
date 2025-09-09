package hashCracker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {
        /*
        checks for the --help flag:
        if it finds it then it prints menu and exits program.
        if it doesn't then it moves on.
         */
        if(supportingFunctions.checkForHelpFlag(args)){
            supportingFunctions.printHelpAndExit();
        }
        /*
        checks for the --list-algorithms flag:
        if it finds it then it prints all algorithms available according to your java
        version and exits program.
        if it doesn't then it moves on.
         */
        if(supportingFunctions.checkForListAlgorithmFlag(args)){
            supportingFunctions.listAlgorithmsAndCloseProgram();
        }

        // calls parseArgs from supportingFunctions and saves them into an array
        String[] parsedArgs = supportingFunctions.parseArgs(args);

        // creates a new Config reference and passes the parsed args into it
        Config params = new Config(parsedArgs);

        // sets the Config in Consts to the config we just created
        Consts.setConfig(params);

        // creates a "timer" to measure time taken to crack the hash
        long startTime = System.nanoTime();

        // creates a new sharedState that will enable communication between threads
        SharedState state = new SharedState();

        /*
        creates the linesQueue and uses BlockingQueue
        to help with multiple threads requesting same material.
        */
        LinkedBlockingQueue<String> linesQueue = new LinkedBlockingQueue    <String>();

        /*
        creates 1 producer threads that reads lines from the file.
        creates 1 progressMonitor threads that will track the progress using the sharedState
         */
        ProducerThreadRunnable producer = new ProducerThreadRunnable(state, linesQueue);
        ProgressMonitorThreadRunnable progressMonitor = new ProgressMonitorThreadRunnable(state, Consts.getConfig().totalLines);

        // creates a thread pool that will run the consumer threads
        ExecutorService consumerThreadExecutor = Executors.newFixedThreadPool(Consts.getConfig().numOfThreads);

        // start the producer thread
        new Thread(producer).start();

        // start the progress monitoring thread
        new Thread(progressMonitor).start();

        /*
            loop that creates Consumer threads based on the number of Threads the user provided.
            if he didn't provide it goes to default.
            then it executes them
        */
        for (int i =0; i<Consts.getConfig().numOfThreads; i++){
            ConsumerThreadRunnable consumer = new ConsumerThreadRunnable(state, linesQueue);
            consumerThreadExecutor.execute(consumer);
        }

        // tells the thread pool to stop accepting new threads
        consumerThreadExecutor.shutdown();

        /*
        tells the mainThread to wait until either all the tasks are finished or time expires.
        however it is set to the max Long Value so program will basically wait until tasks finish
         */
        try {
            consumerThreadExecutor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // catches error if threads get interrupted

            Thread.currentThread().interrupt(); // restore interrupt flag
            System.err.println("Main thread interrupted while waiting for shutdown.");
        }
        //prints time taken
        long endTime = System.nanoTime();
        double timeTaken = (double) (endTime - startTime) /1000000000;
        System.out.println("Time Taken: " + timeTaken + "seconds.");


    }

}


