package hashCracker;

public final class Consts {
    private static Config PARAMS;
    public static final String DEFAULT_FILE_PATH = "C:\\Users\\liav2\\Documents\\Johhny-The-Hasher-Tool\\Johnny\\src\\hashCracker\\rockyou.txt";
    public static final String DEFAULT_ALGORITHM = "SHA-512";
    public static final String DEFAULT_GIVEN_HASH = "9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca72323c3d99ba5c11d7c7acc6e14b8c5da0c4663475c2e5c3adef46f73bcdec043";
    public static final int DEFAULT_NUM_OF_THREADS = 64;
    public static final String POISON_PILL = "END OF PROCESSING 1234567890!@#$%^&*() Inserting Pill: " + DEFAULT_FILE_PATH;
    public static void setConfig(Config c){
        if(PARAMS !=null){
            throw new IllegalArgumentException("Params can only be set once");
        }
        PARAMS = c;
    }
    public static Config getConfig(){
        if(PARAMS ==null){
            throw new IllegalArgumentException("Wait until params are initialized ");
        }
        return PARAMS;
    }

}
