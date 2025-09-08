package hashCracker;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

class SharedState {
    // We make this variable public to allow direct access from other threads
    public final AtomicBoolean found = new AtomicBoolean(false);
    public final AtomicLong processedLines = new AtomicLong(0);

}
