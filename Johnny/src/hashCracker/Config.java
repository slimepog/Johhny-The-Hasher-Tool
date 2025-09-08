package hashCracker;

public final class Config{
    public final String filePath;
    public final String algorithm;
    public final String giveHash;
    public final int numOfThreads;

    public Config(String[] args) {
        this.filePath = args[0];
        this.algorithm = args[1];
        this.giveHash = args[2];
        int temp;
        try {
            temp = Integer.parseInt(args[3]);

            if (temp < 1) {
                throw new IllegalArgumentException("Number of threads must be 1 or more.");
            }
            this.numOfThreads = temp;


        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Number of threads must be an integer.");
        }

    }
}
