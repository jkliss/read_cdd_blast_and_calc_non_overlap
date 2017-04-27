import java.util.List;

/**
 * Created by students on 27.04.17.
 */
public class CDComparator {
    public boolean calculateNonOverlaps(List<ConservedDomain> list){
        boolean fnd = false;
        for (int i = 0; i < list.size() - 1; i++) {
            ConservedDomain domain1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ConservedDomain domain2 = list.get(j);
                if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getStart() && domain2.getEnd() > domain1.getEnd())) {
                    domain1.setNonOverlap();
                    domain2.setNonOverlap();
                    fnd = true;
                }
            }
        }
        return fnd;
    }

    public boolean calculateNonOverlapsWithSize(List<ConservedDomain> list){
        boolean fnd = false;
        for (int i = 0; i < list.size() - 1; i++) {
            ConservedDomain domain1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ConservedDomain domain2 = list.get(j);
                if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getStart() && domain2.getEnd() > domain1.getEnd())) {
                    domain1.setNonOverlap();
                    domain2.setNonOverlap();
                    fnd = true;
                }
            }
        }
        return fnd;
    }

    boolean sizeComparison(ConservedDomain domain1, ConservedDomain domain2){




        return false;
    }
}
