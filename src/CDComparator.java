import java.util.List;

/**
 * Created by students on 27.04.17.
 */
public class CDComparator {
    SeqReader cd_seqs;

    public boolean calculateNonOverlaps(List<ConservedDomain> list){
        if(cd_seqs==null){
            boolean fnd = false;
            for (int i = 0; i < list.size() - 1; i++) {
                ConservedDomain domain1 = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    ConservedDomain domain2 = list.get(j);
                    if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getEnd() && domain2.getEnd() > domain1.getEnd())) {
                        domain1.setNonOverlap();
                        domain2.setNonOverlap();
                        fnd = true;
                    }
                }
            }
            return fnd;
        } else {
            return calculateNonOverlapsWithSize(list);
        }
    }

    public boolean calculateNonOverlapsWithSize(List<ConservedDomain> list){
        boolean fnd = false;
        for (int i = 0; i < list.size() - 1; i++) {
            ConservedDomain domain1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ConservedDomain domain2 = list.get(j);
                if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getEnd() && domain2.getEnd() > domain1.getEnd())) {
                    if(sizeComparison(domain1,domain2)){
                        domain1.setNonOverlap();
                        domain2.setNonOverlap();
                        fnd = true;
                    }
                }
            }
        }
        return fnd;
    }

    boolean sizeComparison(ConservedDomain domain1, ConservedDomain domain2) {
        Protein CDProtein = cd_seqs.get(domain1.name); /** NULL POINTER EXCEPTION ON PROTEIN **/
        int cd_length = CDProtein.getLength();
        int combinedCDAlignmentLength = domain1.getLength() + domain2.getLength();
        System.err.println(combinedCDAlignmentLength + "\t" + cd_length);
        if(combinedCDAlignmentLength >= cd_length*0.5){
            return true;
        }
        return false;
    }

    public void setCd_seqs(SeqReader cd_seqs) {
        this.cd_seqs = cd_seqs;
    }
}
