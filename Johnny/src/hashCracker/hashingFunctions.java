package hashCracker;

import java.security.*;
import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
public class hashingFunctions {
    /**
     *
     * @param str The string we want to hash
     * @param algorithm The hashing algorithm we want to use
     * @return the hex value of the hashed string
     * @exception NoSuchAlgorithmException throw error if algorithm doesn't exist
     */
    public static String hashUsingAlgorithm(String str, MessageDigest hash){
        // gets the message digest object for MD5 hash
        // converts the input to bytes and then updates them in the message digest object
        // use UTF 8 to be able to hash all values from all languages
        hash.reset();
        hash.update(str.getBytes(StandardCharsets.UTF_8));

        // creates a new byte array and digests the input to a md5 hash value of the input in bytes
        // function that converts binary format to hex
        return HexFormat.of().formatHex(hash.digest());
    }
}