/**
 * Created by students on 28.04.17.
 */
public class ElapsedTimer {
    long startTime;
    long time;

    public ElapsedTimer(){
        startTime = System.nanoTime();
        time = startTime;
    }

    public String elapsedFromLastStamp(){
        long time = System.nanoTime();
        long elapsed = time - this.time;
        this.time = time;
        double seconds = (double) elapsed/1000000000;
        String timeStr = "Seconds (from LastStamp): " + Double.toString(seconds);
        System.err.println(timeStr);
        return timeStr;
    }

    public String elapsedFromStart(){
        long time = System.nanoTime();
        long elapsed = time - this.startTime;
        double seconds = (double) elapsed/1000000000;
        String timeStr = "Seconds (from Start): " + Double.toString(seconds);
        System.err.println(timeStr);
        return timeStr;
    }

    public String elapsedFromLastStampIfSignificant(){
        long time = System.nanoTime();
        long elapsed = time - this.time;
        this.time = time;
        double seconds = (double) elapsed/1000000000;
        String timeStr = "Seconds (from LastStamp): " + Double.toString(seconds);
        if(seconds >= 30){
            System.err.println(timeStr);
        }
        return timeStr;
    }
}
