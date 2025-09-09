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
        checks for the --help flag inside the provided arguments:
        if it finds it then it prints the help menu and exits program.
        if it doesn't then it moves on with the code.
         */
        if(supportingFunctions.checkForHelpFlag(args)){
            supportingFunctions.printHelpAndExit();
        }
        /*
        checks for the --list-algorithms flag inside the provided arguments:
        if it finds it then it prints all algorithms available according to your java
        version and exits program.
        if it doesn't then it moves on.
         */
        if(supportingFunctions.checkForListAlgorithmFlag(args)){
            supportingFunctions.listAlgorithmsAndCloseProgram();
        }

        /*
         calls parseArgs from supportingFunctions:
         parseArgs - takes the args provided by the user and iterates through
         the array and returns a String array that will be "readable"
         by the Config class Constructor.
         */
        String[] parsedArgs = supportingFunctions.parseArgs(args);

        /*
        creates a new Config reference for the params and passes the parsed args into it:
        Config is an immutable class that holds the arguments provided by the user and can
        be accessed by the user from Consts
         */
        Config params = new Config(parsedArgs);

        // sets the Config reference of params into Consts
        Consts.setConfig(params);

        // saves Start time of the program so we can later display how long it took it to run
        long startTime = System.nanoTime();

        /*
        creates a new sharedState that will enable communication between threads.
        params in sharedState:
        -found: will indicate to all other threads if the hash has been cracked
        -processedLines: will keep track of number
         of processed lines so we can display it with progressMonitor
         */
        SharedState state = new SharedState();

        /*
        creates the linesQueue and uses BlockingQueue
        to help with multiple threads requesting same material by Blocking 2 threads
        making requests for the same material at the same time.
        */
        LinkedBlockingQueue<String> linesQueue = new LinkedBlockingQueue<String>();

        /*
        creates 1 producer threads that reads lines from the file.
        creates 1 progressMonitor threads that will track the progress using the sharedState and prints
        it while the program runs.
         */
        ProducerThreadRunnable producer = new ProducerThreadRunnable(state, linesQueue);
        ProgressMonitorThreadRunnable progressMonitor = new ProgressMonitorThreadRunnable(state, Consts.getConfig().totalLines);

        // creates a thread pool that will run the consumer threads at the same time
        ExecutorService consumerThreadExecutor = Executors.newFixedThreadPool(Consts.getConfig().numOfThreads);

        // start the producer thread, so it will start populating the linesQueue
        new Thread(producer).start();

        // start the progress monitoring thread so we can get the visual representation of progress
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

        // tells the thread pool to stop accepting new consumer Tasks but to finish the current tasks
        consumerThreadExecutor.shutdown();

        // Wait for all consumers to finish (effectively no timeout).
        try {
            consumerThreadExecutor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // catches error if threads get interrupted

            Thread.currentThread().interrupt(); // restore interrupt flag
            System.err.println("Main thread interrupted while waiting for shutdown.");
        }
        // gets the time when the program ends and calcualtes time taken
        long endTime = System.nanoTime();
        double timeTaken = (double) (endTime - startTime) /1000000000;
        System.out.println("Time Taken: " + timeTaken + "seconds.");


    }

}


