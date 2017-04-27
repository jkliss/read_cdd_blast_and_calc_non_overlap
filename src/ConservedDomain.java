import java.util.List;

public class ConservedDomain {
    int start = 0;
    int end = 0;
    String name = "";
    Protein protein;
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

    public void setNonOverlap() {
        this.hasNonOverlap = true;
        this.__overlapCalcDone = true;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }
}
