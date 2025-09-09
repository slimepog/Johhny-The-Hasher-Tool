package hashCracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;


public final class Config{
    /**
     * @param filePath - filePath of the wordlist which we will compare hashes from
     * @param algorithm - Hashing Algorithm
     * @param givenHash - giveHash from the user
     * @param numOfThreads - number of threads the program will use,
     *                       if none provided then it will be set to the default value.
     * @param totalLines - total lines inside the wordlist
     */

    public final String filePath;
    public final String algorithm;
    public final String givenHash;
    public final int numOfThreads;
    public final long totalLines;

    /**
     *
     * @param args - the array of already parsed args that have a set Order that was predetermined:
     *         args[0] - holds the file path
     *         args[1] - holds the algorithm
     *         args[2] - holds the givenHash
     *         args[3] - holds the number of threads but,
     *         because it is an int then we will have to handle it differently
     */
    public Config(String[] args) {

        this.filePath = args[0];
        this.algorithm = args[1];
        this.givenHash = args[2];

        //handles cases where user provided a String or an illegal amount for numOfThreads
        int tempNumOfThreads;
        try {
            tempNumOfThreads = Integer.parseInt(args[3]);

            if (tempNumOfThreads < 1) {
                throw new IllegalArgumentException("Number of threads must be 1 or more.");
            }
            this.numOfThreads = tempNumOfThreads;


        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Number of threads must be an integer.");
        }

        // iterates through files and counts number of lines
        try {
            this.totalLines = Files.lines(Paths.get(this.filePath), StandardCharsets.ISO_8859_1).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
