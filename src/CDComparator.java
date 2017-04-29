import java.util.List;

/**
 * Created by students on 27.04.17.
 */
public class CDComparator {
    SeqReader cd_seqs;
    boolean ClusterDomainErrorSuppress = false;

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
        try {
            Protein CDProtein1 = cd_seqs.get(domain1.cd_name);
            Protein CDProtein2 = cd_seqs.get(domain2.cd_name);
            int cdlength1 = CDProtein1.getLength();
            int cdlength2 = CDProtein2.getLength();
            if((domain1.length >= cdlength1*0.5) && (domain2.getLength() >= cdlength2*0.5)){
                return true;
            }
        } catch (NullPointerException ex){
            if((!ClusterDomainErrorSuppress) && (!domain1.cd_name.contains("cl") && (!domain2.cd_name.contains("cl")))){
                System.err.println("CD PROTEIN SEQUENCE NOT AVAILABLE: " + domain1.cd_name + "#" + domain2.cd_name);
            } else {
                if(!ClusterDomainErrorSuppress){
                    System.err.println("MISSING CLUSTER PROTEINSEQUENCE -- ERRORS REGARDING CLUSTERS WILL BE IGNORED");
                    ClusterDomainErrorSuppress = true;
                }
            }
        }
        return false;
    }

    public void setCd_seqs(SeqReader cd_seqs) {
        this.cd_seqs = cd_seqs;
    }
}
