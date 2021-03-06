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
    Map<String, List<ConservedDomain>> CDMap = new HashMap<String, List<ConservedDomain>>(10000000);
    SeqReader seqReader;
    Map<String, Boolean> fullGeneCD = new HashMap<String, Boolean>(65000);
    CDComparator cdComparator = new CDComparator();
    Writer writer = new Writer("CDDetectReader.output");
    Writer writerFullGene = new Writer("NoFullGeneDomain.output");
    SeqReader proteinSequences;
    DomainCounter domainCounter = new DomainCounter();

    // READS Complete File with conserved Domains
    // INPUT IS THE OUTFILE OF bwrpls.pl?
    public void initCDDetectReader(String filename) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filename));
            while ((sCurrentLine = br.readLine()) != null) {
                List<ConservedDomain> currentList = new ArrayList<ConservedDomain>();
                String[] splits = sCurrentLine.split("\\s+");
                try {
                    if (CDMap.get(splits[0]) != null) {
                        currentList = CDMap.get(splits[0]);
                    }
                    try{
                        ConservedDomain currentDomain = new ConservedDomain(splits[0], splits[7]);
                        currentDomain.setStartAndEnd(Integer.parseInt(splits[3]), Integer.parseInt(splits[4]));
                        /**
                         * FILTER STEP 3
                         */
                        if (fullGeneCD.containsKey(currentDomain.getCd_name())){
                            if(!currentDomain.isInList(currentList)) {
                                currentList.add(currentDomain);
                                CDMap.put(currentDomain.getProteinName(), currentList);
                            }
                        } else {
                            writerFullGene.writeLine(currentDomain.getProteinName() + " - " + currentDomain.getCd_name());
                        }
                    } catch (IndexOutOfBoundsException ex){
                        ex.printStackTrace();
                        System.err.println(currentList.toString());
                        System.err.println("LINE:" + sCurrentLine);
                    }
                } catch (IllegalStateException ex){
                    ex.printStackTrace();
                    System.err.println("Wrong Format ... COL: 0 Protname; COL: 3 Alignment Start; COL: 4 Alignment End; COL: 7 CDname");
                    System.err.println(sCurrentLine);
                }
            }
            writerFullGene.close();
            System.err.println("Size: " + CDMap.size());
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
            List<ConservedDomain> list = CDMap.get(key);
            if (cdComparator.calculateNonOverlaps(list)) {
                print_subset(key, list);
            }
        }
        writer.flush();
    }

    public void calc_overlap(boolean override) {
        countDomainOccurs();
        for (String key : CDMap.keySet()) {
            List<ConservedDomain> list = CDMap.get(key);
            if (cdComparator.calculateNonOverlaps(list)) {
                print_subset(key, list);
            }
        }
        writer.flush();
    }

    public void print_set(String key) {
        String print = key + "\t";
        for (ConservedDomain domain : CDMap.get(key)) {
            if (!print.contains("(" + domain.getStart() + "," + domain.getEnd() + ")")) {
                print = print + "(" + domain.getStart() + "," + domain.getEnd() + ")";
            }
        }
        System.out.println(print);
    }

    public void print_subset(String key, List<ConservedDomain> list) {
        String print = key + "\t";
        for (ConservedDomain domain : list) {
            if (domain.nonOverlapping() && !print.contains("(" + domain.getStart() + "," + domain.getEnd() + ")")) {
                print = print + "(" + domain.getStart() + "," + domain.getEnd() + ")";
            }
        }
        //System.out.println(print);
        writer.writeLine(print);
    }

    // INPUT IS A FILE WITH RECIPROCAL BLAST WITH QCOVS >= 90%
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

    public void close_writer(){
        writer.close();
    }

    public void setSeqReader(String filename) {
        this.seqReader = new SeqReader(filename);
        cdComparator.setCd_seqs(seqReader); /** To Set Length Comparator **/
    }

    public void printCDs(){
        System.err.println(CDMap.size());
        for (String key : CDMap.keySet()) {
            List<ConservedDomain> domains = CDMap.get(key);
            for (ConservedDomain domain : domains) {
                writer.writeLine(key + "\t" + domain.getCd_name() + "\t" + domain.getLength());
            }
        }
        writer.flush();
    }

    public void setProteinSequences(String filename) {
        proteinSequences = new SeqReader(filename);
        cdComparator.setProteinSequences(proteinSequences);
    }

    public void removeSmallSequences(int lengthThreshold){
        Writer writer = new Writer("SmallSequenceFilter.out");
        Map<String, Protein> pMap = proteinSequences.getSeqMap();
        for (String proteinName : pMap.keySet()) {
            if(pMap.get(proteinName).getLength() < lengthThreshold){
                CDMap.remove(proteinName);
                writer.writeLine("Protein Smaller than "+ lengthThreshold +": " + proteinName + " #" + pMap.get(proteinName).getLength());
            }
        }
        writer.close();
    }

    public void countDomainOccurs(){
        for (String proteinName : CDMap.keySet()) {
            List<ConservedDomain> domains = CDMap.get(proteinName);
            for (int i = 0; i < domains.size() - 1; i++) {
                ConservedDomain domain1 = domains.get(i);
                for (int j = i + 1; j < domains.size(); j++) {
                    ConservedDomain domain2 = domains.get(j);
                    if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getEnd() && domain2.getEnd() > domain1.getEnd())) {
                        domainCounter.addCombination(domain1.getCd_name(), domain2.getCd_name());
                    }
                }
            }
        }
        cdComparator.setDomainCounter(domainCounter);
    }
}