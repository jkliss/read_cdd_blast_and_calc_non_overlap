import java.util.List;

/**
 * Created by students on 25.04.17.
 */
public class ConservedDomain {
    int start = 0;
    int end = 0;
    int length = 0;
    String protein_name = "";
    String cd_name;
    Protein protein;
    boolean __overlapCalcDone;
    boolean hasNonOverlap = false;
    boolean inSizeSpec = false;

    public ConservedDomain(String name){
        this.protein_name = name;
    }

    public ConservedDomain(Protein protein){
        this.protein = protein;
        this.protein_name = protein.getName();
    }

    public ConservedDomain(String protein, String name_of_conserved_domain){
        this.protein = new Protein(protein);
        this.protein_name = this.protein.getName();
        this.cd_name = name_of_conserved_domain;
    }

    public void setStartAndEnd(int start, int end) {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public String getProteinName() {
        return protein_name;
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

    public int getLength() {
        return length;
    }

    public String getCd_name() {
        return cd_name;
    }
}
