package hashCracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;


public final class Config{
    public final String filePath;
    public final String algorithm;
    public final String giveHash;
    public final int numOfThreads;
    public final long totalLines;

    public Config(String[] args) {
        this.filePath = args[0];
        this.algorithm = args[1];
        this.giveHash = args[2];
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
        try {
            this.totalLines = Files.lines(Paths.get(this.filePath), StandardCharsets.ISO_8859_1).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
