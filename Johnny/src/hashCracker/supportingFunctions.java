package hashCracker;

import java.security.*;
import java.util.HashMap;
import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

public class supportingFunctions {
    /**
     * @param str       The string we want to hash
     * @param algorithm The hashing algorithm we want to use
     * @return the hex value of the hashed string
     * @throws NoSuchAlgorithmException throw error if algorithm doesn't exist
     */
    public static String hashUsingAlgorithm(String str, MessageDigest hash) {
        // gets the message digest object for MD5 hash
        // converts the input to bytes and then updates them in the message digest object
        // use UTF 8 to be able to hash all values from all languages
        hash.reset();
        hash.update(str.getBytes(StandardCharsets.UTF_8));

        // creates a new byte array and digests the input to a md5 hash value of the input in bytes
        // function that converts binary format to hex
        return HexFormat.of().formatHex(hash.digest());
    }

    public static String[] parseArgs(String[] args) {
        // Map flags to indexes in the returned 4-element array
        Map<String, Integer> index = Map.of(
                "--file", 0,
                "--algorithm", 1,
                "--giveHash", 2,
                "--threads", 3
        );

        // Initialize parsedArgs: null for all slots; threads (3) will default later if absent
        String[] parsedArgs = new String[4]; // [file, algorithm, giveHash, threads]

        java.util.List<String> positional = new java.util.ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String token = args[i];

            // Reject inline form explicitly
            if (token.startsWith("-") && token.contains("=")) {
                throw new IllegalArgumentException("Inline flag syntax not supported: '" + token + "'. Use '--flag value'.");
            }

            // Space-separated flag: --flag value
            if (token.startsWith("-")) {
                if (!index.containsKey(token)) {
                    throw new IllegalArgumentException("Unknown flag: '" + token + "'");
                }

                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Flag '" + token + "' requires a value but none was provided.");
                }

                String next = args[++i];

                if (next.startsWith("-") || next.isEmpty()) {
                    throw new IllegalArgumentException("Flag '" + token + "' requires a non-empty value, but got: '" + next + "'");
                }

                parsedArgs[index.get(token)] = next;
                System.out.println("✅ '" + token + "' set to '" + next + "'");
                continue;
            }

            // Positional argument (no leading dash)
            positional.add(token);
            System.out.println("📄 Positional argument: '" + token + "'");
        }

        // Check required flags (all except threads)
        if (parsedArgs[0] == null) {
            throw new IllegalArgumentException("--file is required.");
        }
        if (parsedArgs[1] == null) {
            throw new IllegalArgumentException("--algorithm is required.");
        }
        if (parsedArgs[2] == null) {
            throw new IllegalArgumentException("--giveHash is required.");
        }

        // Set default for threads if not provided
        if (parsedArgs[3] == null) {
            parsedArgs[3] = String.valueOf(Consts.DEFAULT_NUM_OF_THREADS);
            System.out.println("ℹ️ --threads not provided, using default: " + parsedArgs[3]);
        }

        // Debug print final parsed values
        System.out.println("\n🔍 Final Parsed Arguments:");
        System.out.println("File Path     : " + parsedArgs[0]);
        System.out.println("Algorithm     : " + parsedArgs[1]);
        System.out.println("Give Hash     : " + parsedArgs[2]);
        System.out.println("Num of Threads: " + parsedArgs[3]);

        if (!positional.isEmpty()) {
            System.out.println("Positional args were provided: " + positional);
        }

        return parsedArgs;
    }

    public static boolean checkForListAlgorithmFlag(String[] args){
        for (String arg : args){
            if(arg.equalsIgnoreCase("--list-algorithms")){
                return true;
            }
        }
        return false;
    }
    public static boolean checkForHelpFlag(String[] args){
        for (String arg : args){
            if(arg.equalsIgnoreCase("--help")){
                return true;
            }
        }
        return false;
    }
    public static void listAlgorithmsAndCloseProgram(){
        Set<String> algos = Security.getAlgorithms("MessageDigest");
        System.out.println("Supported Algorithms are: ");
        for(String algo : algos){
            System.out.println("- " +algo);
        }
        System.exit(0);
    }
    public static boolean hasHelpFlag(String[] args) {
        for (String arg : args) {
            if ("--help".equalsIgnoreCase(arg) || "-h".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public static void printHelpAndExit() {
        System.out.println("📘 HashCracker Tool - Help\n");
        System.out.println("Usage:");
        System.out.println("  java hashCracker.Main --file <wordlist.txt> --algorithm <algo> --giveHash <hash> [--threads <n>]");
        System.out.println("\nRequired arguments:");
        System.out.println("  --file <path>        Path to the wordlist file (e.g. rockyou.txt)");
        System.out.println("  --algorithm <algo>   Hashing algorithm to use (e.g. SHA-256, MD5)");
        System.out.println("  --giveHash <hash>    The hash string you want to crack");
        System.out.println("\nOptional arguments:");
        System.out.println("  --threads <n>        Number of worker threads (default: " + Consts.DEFAULT_NUM_OF_THREADS + ")");
        System.out.println("  --list-algorithms    List all supported MessageDigest algorithms");
        System.out.println("  --help, -h           Show this help message and exit");
        System.out.println("\nExamples:");
        System.out.println("  java hashCracker.Main --file rockyou.txt --algorithm SHA-256 --giveHash deadbeef...");
        System.out.println("  java hashCracker.Main --list-algorithms");
        System.out.println("  java hashCracker.Main --help");
        System.exit(0);
    }




}