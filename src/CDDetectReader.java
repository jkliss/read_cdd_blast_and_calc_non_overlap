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
public class CDDetectReader {
    Map<String, List<int[]>> CDMap = new HashMap<String, List<int[]>>(10000000);

    public CDDetectReader(String filename){
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
                List<int[]> currentList = new ArrayList<int[]>();
                try{
                    if(CDMap.get(match.group(1)) != null){
                        currentList = CDMap.get(match.group(1));
                    }
                    if(!currentList.contains(new int[]{Integer.parseInt(match.group(2)), Integer.parseInt(match.group(3))})){
                        currentList.add(new int[]{Integer.parseInt(match.group(2)), Integer.parseInt(match.group(3))});
                        CDMap.put(match.group(1), currentList);
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

    public void calc_overlap(){
        for (String key : CDMap.keySet()) {
            boolean fnd = false;
            List<int[]> list = CDMap.get(key);
            boolean[] hasNoOverlap = new boolean[list.size()];
            for(int i = 0; i < list.size()-1; i++){
                int[] ints1 = list.get(i);
                for(int j = i+1; j < list.size(); j++){
                    int[] ints2 = list.get(j);
                    if((ints1[0] > ints2[1] && ints1[1] > ints2[1] ) || (ints2[0] > ints1[1] && ints2[1] > ints1[1])){
                        hasNoOverlap[i] = true;
                        hasNoOverlap[j] = true;
                        fnd = true;
                    }
                }
            }
            if(fnd){
                print_subset(key, list, hasNoOverlap);
            }
        }
    }

    public void print_set(String key){
        String print = key + "\t";
        for (int[] ints : CDMap.get(key)) {
            if(!print.contains("(" + ints[0] + "," + ints[1] + ")")){
                print = print + "(" + ints[0] + "," + ints[1] + ")";
            }
        }
        System.out.println(print);
    }

    public void print_subset(String key, List<int[]> list,boolean[] hasNoOverlap){
        String print = key + "\t";
        for (int i = 0; i < list.size(); i++) {
            if(hasNoOverlap[i] && !print.contains("(" + list.get(i)[0] + "," + list.get(i)[1] + ")")){
                print = print + "(" + list.get(i)[0] + "," + list.get(i)[1] + ")";
            }
        }
        System.out.println(print);
    }
}
