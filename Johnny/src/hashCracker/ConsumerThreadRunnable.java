package hashCracker;

import java.security.MessageDigest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConsumerThreadRunnable implements Runnable {
    private final SharedState state;
    private BlockingQueue<String> linesQueue;
    //just so we dont create new messageDigest and digest ever run
    private MessageDigest messageDigest;

    public ConsumerThreadRunnable(SharedState state, BlockingQueue<String> linesQueue) {
        this.state = state;
        this.linesQueue = linesQueue;
        try {
            this.messageDigest = MessageDigest.getInstance(Consts.algorithm);
        }
        catch (Exception NoSuchAlgorithmException){
            System.out.println("Hashing Algorithm not Found!");
        }

    }
    @Override
    public void run() {
        try {
            String nextLine = "", hash;
            while (true) {
                nextLine = this.linesQueue.take();
                if (Consts.poisonPill.equals(nextLine)) break;
                hash = hashingFunctions.hashUsingAlgorithm(nextLine, this.messageDigest);
                if (this.state.found.get()) break;

                if (hash.equals(Consts.givenHash)) {
                    printFound(hash, nextLine);
                    this.state.found.set(true);
                    break;
                }
            }
        }
        catch (Exception e){
                throw new RuntimeException(e);
        }

    }
    public static void printFound(String hash, String line){
        System.out.println("Hash Was Cracked!\nAlgorithm used: " +
                Consts.algorithm +"\nHash: " + Consts.givenHash + " Value: " + line
                + "\nThanks for Using Johnny the hasher");
    }
}