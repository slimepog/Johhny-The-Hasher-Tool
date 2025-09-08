import java.security.Security;
import java.security.Provider;
import java.util.Set;

public class ListMessageDigestAlgos {
    public static void main(String[] args) {
        Set<String> algorithms = Security.getAlgorithms("MessageDigest");
        for (String algorithm : algorithms) {
            System.out.println(algorithm);
        }
    }
}
