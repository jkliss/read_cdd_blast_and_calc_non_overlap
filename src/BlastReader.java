import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by students on 07.04.17.
 *
 */
public class BlastReader {
    Map<String, List<ConservedDomain>> blastMap = new HashMap<String, List<ConservedDomain>>(10000000);
    Writer writer = new Writer("ReadCDDBlastNonOverlap.output");

    public BlastReader(String filename){
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            Pattern pattern = Pattern.compile("([^\\s]+)\\((\\d+),(\\d+)\\)");
            while ((sCurrentLine = br.readLine()) != null) {
                Matcher match = pattern.matcher(sCurrentLine);
                match.find();
                List<ConservedDomain> currentList = new ArrayList<ConservedDomain>();
                try{
                    if(blastMap.get(match.group(1)) != null){
                        currentList = blastMap.get(match.group(1));
                    }
                    ConservedDomain currentDomain = new ConservedDomain(match.group(1));
                    currentDomain.setStartAndEnd(Integer.parseInt(match.group(2)), Integer.parseInt(match.group(3)));
                    if(!currentDomain.isInList(currentList)){
                        currentList.add(currentDomain);
                        blastMap.put(match.group(1), currentList);
                        //System.out.println(match.group(1) + " " + match.group(2) + " " + match.group(3));
                    }
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

    public void print_set(String key) {
        String print = key + "\t";
        for (ConservedDomain domain : blastMap.get(key)) {
            if (!print.contains("(" + domain.getStart() + "," + domain.getEnd() + ")")) {
                print = print + "(" + domain.getStart() + "," + domain.getEnd() + ")";
            }
        }
        System.out.println(print);
    }

    public void calc_overlap(){
        for (String key : blastMap.keySet()) {
            List<ConservedDomain> list = blastMap.get(key);
            CDComparator cdComparator = new CDComparator();
            if (cdComparator.calculateNonOverlaps(list)) {
                print_subset(key, list);
            }
        }
    }

    public void calc_overlap_with_size(){
        for (String key : blastMap.keySet()) {
            List<ConservedDomain> list = blastMap.get(key);
            CDComparator cdComparator = new CDComparator();
            if (cdComparator.calculateNonOverlaps(list)) {
                print_subset(key, list);
            }
        }
    }

    public void print_subset(String key, List<ConservedDomain> list) {
        String print = key + "\t";
        for (ConservedDomain domain : list) {
            if (domain.nonOverlapping() && !print.contains("(" + domain.getStart() + "," + domain.getEnd() + ")")) {
                print = print + "(" + domain.getStart() + "," + domain.getEnd() + ")";
            }
        }
        System.out.println(print);
        writer.writeLine(print);
    }

    public void close_writer(){
        writer.close();
    }
}
