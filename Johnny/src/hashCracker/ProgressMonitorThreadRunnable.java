package hashCracker;

public class ProgressMonitorThreadRunnable implements Runnable {
    private final SharedState state;
    private final long totalLines;
    public ProgressMonitorThreadRunnable(SharedState state, long totalLines){
        this.state = state;
        this.totalLines = totalLines;
    }
    @Override
    public void run(){
        int barWidth = 50;
        double progress = (double) this.state.processedLines.get() / this.totalLines;
        int filled = (int) (barWidth * progress);
        String bar = "[" + "=".repeat(filled) + " ".repeat(barWidth - filled) + "]";
        while (!this.state.found.get() && this.totalLines > this.state.processedLines.get()) {
            progress = (double) this.state.processedLines.get() / this.totalLines;
            filled = (int) (barWidth * progress);
            bar = "[" + "=".repeat(filled) + " ".repeat(barWidth - filled) + "]";
            System.out.printf("\r%s %.2f%% (%d/%d)", bar, progress * 100, this.state.processedLines.get(), totalLines);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("\r%s %.2f%% (%d/%d)", bar, progress * 100, this.state.processedLines.get(), totalLines);
    }

}
