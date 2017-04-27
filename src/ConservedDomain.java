import java.util.List;
import java.util.Map;

/**
 * Created by students on 25.04.17.
 */
public class ConservedDomain {
    int start = 0;
    int end = 0;
    String name = "";
    String cd_name;
    Protein protein;
    boolean __overlapCalcDone;
    boolean hasNonOverlap = false;
    boolean inSizeSpec = false;

    public ConservedDomain(String name){
        this.name = name;
    }

    public ConservedDomain(Protein protein){
        this.name = protein.getName();
        this.protein = protein;
    }

    public ConservedDomain(String protein, String name_of_conserved_domain){
        this.protein = new Protein(protein);
        this.cd_name = name_of_conserved_domain;
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

    public void setInSizeSpec() {
        this.inSizeSpec = true;
    }


}
