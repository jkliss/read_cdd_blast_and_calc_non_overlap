import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by students on 27.04.17.
 */
public class SeqReader {
    Map<String, Protein> seqMap = new HashMap<String, Protein>(1000);
    Writer writer = new Writer("SeqReader.output");

    public SeqReader(String filename){
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
                        if(currentHead.length() > 1){
                            Protein currentProtein = new Protein(currentHead, currentSeq);
                            seqMap.put(currentHead, currentProtein);
                        }
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
}
