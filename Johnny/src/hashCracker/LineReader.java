package hashCracker;

import java.io.*;

public class LineReader {
    private BufferedReader reader;

    public LineReader(String filePath) throws IOException{
        this.reader = new BufferedReader(new FileReader(filePath));
    }
    public String readNextLine() throws IOException{
        return this.reader.readLine();
    }
    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }
}
