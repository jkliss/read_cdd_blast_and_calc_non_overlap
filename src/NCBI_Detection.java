import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by students on 09.05.17.
 */
public class NCBI_Detection {
    public boolean useSequences = false;
    public int SIZE_OF_QUERY = 4000;
    SeqReader sequences;
    String[] proteinList = new String[SIZE_OF_QUERY];
    int set = 0;
    Thread[] threads = new Thread[8];

    public static void main(String[] args) {
        if(args.length > 0){
            NCBI_Detection ncbi_detection = new NCBI_Detection(new SeqReader(args[0]));
            if(args.length > 1){
                if(args.length == 2 && args[1].contains("-seq")){
                    ncbi_detection.setUseSequences();
                } else if(args.length == 2 && args[1].contains("-single")){
                    System.out.println("Use Single...");
                    ncbi_detection.setSingleSeq();
                } else {
                    ncbi_detection = new NCBI_Detection(new SeqReader(args[0]), Integer.parseInt(args[1]));
                }
            }
            ncbi_detection.start();
        } else {
            System.err.println("Argument 1: Input Sequence File\nOptional Argument Number of threads (std=8)\nOr -seq to include sequences");
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
            if(i < SIZE_OF_QUERY-1){
                proteinList[i] = protein;
                if(useSequences){
                    proteinList[i] = sequences.get(protein).getProteinAsFasta();
                }
                i++;
            } else {
                i = 0;
                String name = "proteinSet_" + set;
                Writer writer = new Writer(name);
                writer.writeAllLines(proteinList);
                writer.close();
                proteinList = new String[SIZE_OF_QUERY];
                set++;
            }
        }
        String name = "proteinSet_" + set;
        Writer writer = new Writer(name);
        writer.writeAllLines(proteinList);
        writer.close();
        startAll();
    }

    public void startAll(){
        for(int z = 0; z <= set;){
            for(int i = 0; i < threads.length; i++){
                if(threads[i] == null || !threads[i].isAlive()){
                    //System.err.println("Thread " + z);
                    threads[i] = scheduler(z);
                    threads[i].start();
                    System.out.println("Thread started...");
                    z++;
                    try {
                        Thread.sleep(2000);
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
                    Thread.sleep(2000);
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
                    System.out.println("Protein Set " + i + " started.");
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
                    if((line = in.readLine()) != null){
                        if(line.contains("failed")){
                            System.out.println("Search " + i + " failed!");
                        } else {
                            writer.writeLine(line);
                        }
                    } else {
                        System.out.println("ok!");
                    }
                    while ((line = in.readLine()) != null) {
                        writer.writeLine(line);
                    }
                    writer.flush();
                    pr.waitFor();
                    in.close();
                    writer.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Protein Set " + i + " ended");
            }
        });
        return thread;
    }

    public void setUseSequences() {
        this.useSequences = true;
        this.SIZE_OF_QUERY = 10;
        this.proteinList = new String[SIZE_OF_QUERY];
    }

    public void setSingleSeq(){
        this.SIZE_OF_QUERY = 2;
        this.proteinList = new String[SIZE_OF_QUERY];
    }
}
