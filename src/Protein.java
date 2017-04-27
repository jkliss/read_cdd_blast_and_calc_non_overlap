import java.util.List;

/**
 * Created by students on 26.04.17.
 */
public class Protein {
    String name;
    String seq;
    int length;

    public Protein(String name){
        this.name = name;
    }

    public Protein(String name, String seq){
        this.name = name;
        this.seq = seq;
        length = seq.length();
    }

    public String getName() {
        return name;
    }

    public boolean containedIn(List<Protein> proteinList){
        for (Protein protein : proteinList) {
            if(protein.getName() == name){
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return length;
    }
}
