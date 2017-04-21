import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by students on 07.04.17.
 *
 */
public class CDDetectReader {
    Map<String, List<int[]>> CDMap = new HashMap<String, List<int[]>>(10000000);
    Map<String, Boolean> fullGeneCD = new HashMap<String, Boolean>(1000000);

    public void initCDDetectReader(String filename) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                List<int[]> currentList = new ArrayList<int[]>();
                String[] splits = sCurrentLine.split("\t");
                try {
                    if (CDMap.get(splits[0]) != null) {
                        currentList = CDMap.get(splits[0]);
                    }
                    if (fullGeneCD.containsKey(splits[7]) && !currentList.contains(new int[]{Integer.parseInt(splits[3]), Integer.parseInt(splits[4])})) {
                        currentList.add(new int[]{Integer.parseInt(splits[4]), Integer.parseInt(splits[4])});
                        CDMap.put(splits[0], currentList);
                    }
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    System.err.println("Wrong Format ... COL: 0 Protname; COL: 3 Alignment Start; COL: 4 Alignment End; COL: 7 CDname");
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

    public void calc_overlap() {
        for (String key : CDMap.keySet()) {
            boolean fnd = false;
            List<int[]> list = CDMap.get(key);
            boolean[] hasNoOverlap = new boolean[list.size()];
            for (int i = 0; i < list.size() - 1; i++) {
                int[] ints1 = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    int[] ints2 = list.get(j);
                    if ((ints1[0] > ints2[1] && ints1[1] > ints2[1]) || (ints2[0] > ints1[1] && ints2[1] > ints1[1])) {
                        hasNoOverlap[i] = true;
                        hasNoOverlap[j] = true;
                        fnd = true;
                    }
                }
            }
            if (fnd) {
                print_subset(key, list, hasNoOverlap);
            }
        }
    }

    public void print_set(String key) {
        String print = key + "\t";
        for (int[] ints : CDMap.get(key)) {
            if (!print.contains("(" + ints[0] + "," + ints[1] + ")")) {
                print = print + "(" + ints[0] + "," + ints[1] + ")";
            }
        }
        System.out.println(print);
    }

    public void print_subset(String key, List<int[]> list, boolean[] hasNoOverlap) {
        String print = key + "\t";
        for (int i = 0; i < list.size(); i++) {
            if (hasNoOverlap[i] && !print.contains("(" + list.get(i)[0] + "," + list.get(i)[1] + ")")) {
                print = print + "(" + list.get(i)[0] + "," + list.get(i)[1] + ")";
            }
        }
        System.out.println(print);
    }

    public void setFullGeneCD(String filename) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                sCurrentLine = sCurrentLine.replace("\n", "");
                fullGeneCD.put(sCurrentLine, true);
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