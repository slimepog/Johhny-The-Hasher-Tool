package hashCracker;
import java.util.concurrent.BlockingQueue;


public class ProducerThreadRunnable implements Runnable{
    private final SharedState state;
    private BlockingQueue<String> linesQueue;
    public ProducerThreadRunnable( SharedState state, BlockingQueue<String> linesQueue){
        this.state = state;
        this.linesQueue = linesQueue;
    }
    @Override
    public void run() {
        try {
            LineReader reader = new LineReader(Consts.getConfig().filePath);
            String line;
            while(!state.found.get()){
                line = reader.readNextLine();
                if(line == null){
                    insertPoisonPill();
                    break;
                }
                this.linesQueue.put(line);
            }
            reader.close();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    public void insertPoisonPill(){
        for(int i =0; i<Consts.getConfig().numOfThreads; i++){
            try {
                this.linesQueue.put(Consts.POISON_PILL);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

