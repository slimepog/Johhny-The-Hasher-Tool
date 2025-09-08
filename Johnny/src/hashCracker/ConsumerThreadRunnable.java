package hashCracker;

import java.security.MessageDigest;
import java.util.concurrent.BlockingQueue;

public class ConsumerThreadRunnable implements Runnable {
    private final SharedState state;
    private BlockingQueue<String> linesQueue;
    //just so we dont create new messageDigest and digest ever run
    private MessageDigest messageDigest;

    public ConsumerThreadRunnable(SharedState state, BlockingQueue<String> linesQueue) {
        this.state = state;
        this.linesQueue = linesQueue;
        try {
            this.messageDigest = MessageDigest.getInstance(Consts.getConfig().algorithm);
        }
        catch (Exception NoSuchAlgorithmException){
            System.out.println("Hashing Algorithm not Found!");
        }

    }
    @Override
    public void run() {
        try {
            String givenHash = Consts.getConfig().giveHash;
            String nextLine = "", hash;
            while (true) {
                nextLine = this.linesQueue.take();
                if (Consts.POISON_PILL.equals(nextLine)) break;
                hash = supportingFunctions.hashUsingAlgorithm(nextLine, this.messageDigest);
                if (this.state.found.get()) break;

                if (hash.equals(givenHash)) {
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
                Consts.getConfig().algorithm +"\nHash: " + Consts.getConfig().giveHash + " Value: " + line
                + "\nThanks for Using Johnny the hasher");
    }
}