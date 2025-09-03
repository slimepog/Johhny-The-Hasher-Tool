package hashCracker;
import java.util.concurrent.atomic.AtomicBoolean;

class SharedState {
    // We make this variable public to allow direct access from other threads
    public final AtomicBoolean found = new AtomicBoolean(false);
}
