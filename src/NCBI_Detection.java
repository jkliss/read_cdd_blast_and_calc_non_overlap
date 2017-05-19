import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by students on 09.05.17.
 */
public class NCBI_Detection {
    SeqReader sequences;
    String[] proteinList = new String[4000];
    int set = 0;
    Thread[] threads = new Thread[8];

    public static void main(String[] args) {
        if(args.length > 0){
            NCBI_Detection ncbi_detection = new NCBI_Detection(new SeqReader(args[0]));
            if(args.length > 1){
                ncbi_detection = new NCBI_Detection(new SeqReader(args[0]), Integer.parseInt(args[1]));
            }
            ncbi_detection.start();
        } else {
            System.err.println("Argument 1: Input Sequence File\nOptional Argument Number of threads (std=8)");
        }
    }

    public NCBI_Detection(SeqReader sequences){
        this.sequences = sequences;
    }

    public NCBI_Detection(SeqReader sequences, int numThreads){
        this.sequences = sequences;
        threads = new Thread[numThreads];
    }

    public void start(){
        Map<String, Protein> proteinMap = sequences.getSeqMap();
        int i = 0;
        set = 0;
        for (String protein : proteinMap.keySet()) {
            if(i < 3999){
                proteinList[i] = protein;
                i++;
            } else {
                i = 0;
                String name = "proteinSet_" + set;
                Writer writer = new Writer(name);
                writer.writeAllLines(proteinList);
                proteinList = new String[4000];
                set++;
            }
        }
        String name = "proteinSet_" + set;
        Writer writer = new Writer(name);
        writer.writeAllLines(proteinList);
        startAll();
    }

    public void startAll(){
        for(int z = 0; z <= set;){
            for(int i = 0; i < threads.length; i++){
                if(threads[i] == null || !threads[i].isAlive()){
                    //System.err.println("Thread " + z);
                    threads[i] = scheduler(z);
                    threads[i].start();
                    System.err.println("Thread started...");
                    z++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        for(int i = 0; i < threads.length; i++){
            if(threads[i] != null && threads[i].isAlive()){
                i = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Thread scheduler(final int i){
        Thread thread = new Thread(new Thread() {
            public void run() {
                try{
                    System.err.println("Protein Set " + i + " started.");
                    //ProcessBuilder ps=new ProcessBuilder("./mod_bwrpsd.pl", "< proteinSet_" + i, "1> proteinSet_" + i + ".out", "2> proteinSet_" + i + ".err");
                    ProcessBuilder ps = new ProcessBuilder("./mod_bwrpsd.pl");
                    //ProcessBuilder ps = new ProcessBuilder("echo");
                    /**From the DOC:  Initially, this property is false, meaning that the
                    //standard output and error output of a subprocess are sent to two
                    separate streams **/
                    ps.redirectErrorStream(true);
                    Process pr = null;
                    pr = ps.start();
                    Writer writer = new Writer("proteinSet_" + i + ".out");
                    OutputStream stdin = pr.getOutputStream();
                    BufferedWriter stdin_writer = new BufferedWriter(new OutputStreamWriter(stdin));
                    Reader reader = new Reader("proteinSet_" + i);

                    String line;
                    while((line = reader.readLine()) != null) {
                        stdin_writer.write(line);
                        stdin_writer.newLine();
                    }
                    stdin_writer.flush();
                    stdin_writer.close();

                    pr.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    while ((line = in.readLine()) != null) {
                        writer.writeLine(line);
                    }
                    writer.flush();
                    pr.waitFor();
                    System.out.println("ok!");
                    in.close();
                    writer.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.err.println("Protein Set " + i + " ended");
            }
        });
        return thread;
    }
}
