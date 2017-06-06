import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by students on 27.04.17.
 */
public class SeqReader {
    Map<String, Protein> seqMap = new HashMap<String, Protein>();
    Writer writer = new Writer("SeqReader.output");

    public SeqReader(String filename){
        initMap(filename); //Initialize HashMap Depending on Filesize
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            String currentHead = "";
            String currentSeq = "";
            while ((sCurrentLine = br.readLine()) != null) {
                try{
                    if(sCurrentLine.contains(">")){
                        sCurrentLine = sCurrentLine.replace(">", "");
                        // If not first run, old protein write to hash
                        if(currentHead.length() > 1){
                            Protein currentProtein = new Protein(currentHead, currentSeq);
                            seqMap.put(currentHead, currentProtein);
                        }
                        currentSeq = "";
                        currentHead = sCurrentLine;
                    } else {
                        sCurrentLine = sCurrentLine.replace("\n", "");
                        currentSeq += sCurrentLine;
                    }
                } catch (IllegalStateException ex){
                    ex.printStackTrace();
                    System.err.println("Wrong Format?");
                    System.err.println(sCurrentLine);
                }
            }
            // If last not contained -> Last flush to hash
            if(!seqMap.containsKey(currentHead)){
                Protein currentProtein = new Protein(currentHead, currentSeq);
                seqMap.put(currentHead, currentProtein);
            }
            writeSeqLength();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initMap(String filename){
        /**
         * Average Sequence with 400 Proteins.
         * 250 as estimate with air to top :)
         */
        File file = new File(filename);
        seqMap = new HashMap<String, Protein>((int) (file.length()/250));
    }

    public int sizeOf(String protName){
        return seqMap.get(protName).getLength();
    }

    public Protein get(String proteinName){
        return seqMap.get(proteinName);
    }

    public int getLength(String proteinName){
        return seqMap.get(proteinName).getLength();
    }

    public Map<String, Protein> getSeqMap() {
        return seqMap;
    }

    public void removeSeqsToSaveSpace(){
        for (Protein protein : seqMap.values()) {
            protein.removeSeq();
        }
    }

    public void writeSeqLength(){
        for (Protein protein : seqMap.values()) {
            writer.writeLine(protein.getName() + "\t" + protein.getLength());
        }
        writer.flush();
    }

    public int size(){
        return seqMap.size();
    }

    public boolean hasSequence(String seq){
        return seqMap.containsKey(seq);
    }
}
