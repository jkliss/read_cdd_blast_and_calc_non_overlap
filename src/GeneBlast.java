import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by students on 27.04.17.
 */
public class GeneBlast {
    Map<String, List<ConservedDomain>> blastMap = new HashMap<String, List<ConservedDomain>>(10000000);
    Writer writer = new Writer("TEST.output");

    public GeneBlast(String filename){
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                try{
                    String[] splits = sCurrentLine.split("\t");

                } catch (IllegalStateException ex){
                    ex.printStackTrace();
                    System.err.println("Wrong Format ... ( has to be CDD(X,X) )");
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


}
