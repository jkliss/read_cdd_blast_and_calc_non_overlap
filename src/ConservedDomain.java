import java.util.List;

/**
 * Created by students on 25.04.17.
 */
public class ConservedDomain {
    int start = 0;
    int end = 0;
    String name = "";
    boolean __overlapCalcDone;
    boolean hasNonOverlap = false;

    public ConservedDomain(String name){
        this.name = name;
    }

    public void setStartAndEnd(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public String getName() {
        return name;
    }

    public boolean isInList(List<ConservedDomain> list){
        for (ConservedDomain domain : list) {
            if(domain.getStart() == start && domain.getEnd() == end){
                return true;
            }
        }
        return false;
    }

    public boolean nonOverlapping(){
        if(!__overlapCalcDone){
            System.err.println("No Overlap Calculation done!");
        }
        return hasNonOverlap;
    }

    public static boolean calculateNonOverlaps(List<ConservedDomain> list){
        boolean fnd = false;
        for (int i = 0; i < list.size() - 1; i++) {
            ConservedDomain domain1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ConservedDomain domain2 = list.get(j);
                if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getStart() && domain2.getEnd() > domain1.getEnd())) {
                    domain1.hasNonOverlap();
                    domain2.hasNonOverlap();
                    fnd = true;
                }
            }
        }
        return fnd;
    }

    private void hasNonOverlap() {
        this.hasNonOverlap = true;
        this.__overlapCalcDone = true;
    }
}
