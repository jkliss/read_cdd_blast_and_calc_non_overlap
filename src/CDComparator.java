import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by students on 27.04.17.
 */
public class CDComparator {
    SeqReader cd_seqs;
    SeqReader proteinSequences;
    boolean ClusterDomainErrorSuppress = false;
    List<List<ConservedDomain>> domainsPerPosition;
    Writer writer = new Writer("CDComparator.output");

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
        domainsInGaps(list); // PRECALCULATION FOR STEP 10
        boolean fnd = false;
        for (int i = 0; i < list.size() - 1; i++) {
            ConservedDomain domain1 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                ConservedDomain domain2 = list.get(j);
                /**
                 * FILTER STEP 2
                 */
                if ((domain1.getStart() > domain2.getEnd() && domain1.getEnd() > domain2.getEnd()) || (domain2.getStart() > domain1.getEnd() && domain2.getEnd() > domain1.getEnd())) {
                    if(sizeComparison(domain1,domain2) && gapBetweenDomainsNotAtEnd(domain1,domain2)){
                        if(lessThanHalfInGaps(domain1, domain2, list.size())){
                            domain1.setNonOverlap();
                            domain2.setNonOverlap();
                            fnd = true;
                        }
                    }
                }
            }
        }
        writer.flush();
        return fnd;
    }

    boolean sizeComparison(ConservedDomain domain1, ConservedDomain domain2) {
        try {
            Protein CDProtein1 = cd_seqs.get(domain1.cd_name);
            Protein CDProtein2 = cd_seqs.get(domain2.cd_name);
            int cdlength1 = CDProtein1.getLength();
            int cdlength2 = CDProtein2.getLength();
            /**
             * FILTER STEP 5
             * **/
            if((domain1.length < 50) || (domain2.getLength() < 50)){
                writer.writeLine("DOMAIN 1 OR 2 LENGTH TOO SHORT: " + domain1.getName() + " - " + domain1.getCd_name() + " " + domain1.getLength() + " - " + domain2.getCd_name() + " " + domain2.getLength());
                return false;
            }
            try {
                /**
                 * FILTER STEP 7
                 * */
                if (cdSequenceSmallerThanPercent(proteinSequences.get(domain1.getName()), domain1.getLength() + domain2.getLength(), 0.4)){
                    writer.writeLine("CD SEQUENCE LESS THAN 40%: " + domain1.getName() + "#" + proteinSequences.get(domain1.getName()).getLength() + " - " + domain1.getCd_name() + " " + domain1.getLength() + " - " + domain1.getCd_name() + " " + domain1.getLength());
                    return false;
                }
            } catch (NullPointerException ex){
                System.err.println("MISSING PROTEIN SEQUENCE: " + domain1.getName());
            }
            /**
             * FILTER STEP 4
             **/
            if((domain1.getLength() >= cdlength1*0.5) && (domain2.getLength() >= cdlength2*0.5)){
                return true;
            } else {
                writer.writeLine("DOMAIN CMP FULLGENE TOO SMALL " + domain1.getName() + "#" + domain1.getLength() + "/" + cdlength1*0.5 + " - " + domain2.getName() + "#" + domain2.getLength() + "/" + cdlength2*0.5);
                return false;
            }
        } catch (NullPointerException ex){
            if((!ClusterDomainErrorSuppress) && (!domain1.cd_name.contains("cl") && (!domain2.cd_name.contains("cl")))){
                System.err.println("CD PROTEIN SEQUENCE NOT AVAILABLE: " + domain1.cd_name + "#" + domain2.cd_name);
            } else if(!ClusterDomainErrorSuppress){
                    System.err.println("MISSING CLUSTER PROTEINSEQUENCE -- ERRORS REGARDING CLUSTERS WILL BE IGNORED");
                    ClusterDomainErrorSuppress = true;
            }
        }
        if((!domain1.cd_name.contains("cl") && (!domain2.cd_name.contains("cl")))){
            writer.writeLine("DOMAIN TOO SMALL: (" + domain1.getName() + ")" + domain1.getCd_name() + " - " + domain1.getLength() + " / " + cd_seqs.get(domain1.cd_name).getLength() + " ### " + domain2.getCd_name() + " - " + domain2.getLength() + " / " + cd_seqs.get(domain2.cd_name).getLength());
        }
        return false;
    }

    public void setCd_seqs(SeqReader cd_seqs) {
        this.cd_seqs = cd_seqs;
    }

    public void setProteinSequences(SeqReader proteinSequences) {
        this.proteinSequences = proteinSequences;
    }

    public boolean cdSequenceSmallerThanPercent(Protein protein, int sumLengthOfCD, double percent){
        if(protein.getLength() <= sumLengthOfCD*percent){
            return true;
        }
        return false;
    }

    public boolean gapBetweenDomainsNotAtEnd(ConservedDomain domain1, ConservedDomain domain2){
        /**
         * FILTER STEP 8
         */
        Protein protein = proteinSequences.get(domain1.getName());
        int proteinLength = protein.getLength();
        int diff = 0;
        if(domain1.getEnd() < domain2.getStart()){
            diff = proteinLength - domain2.getStart();
            if(diff > 59 && diff >= proteinLength*0.1){
                return true;
            }
        } else if(domain2.getEnd() < domain1.getStart()){
            diff = protein.length - domain1.getStart();
            if(diff > 59 && diff >= proteinLength*0.1){
                return true;
            }
        }
        writer.writeLine("INSUFFICIENT GAP OR SECOND DOMAIN NOT IN ENDREGION: " + diff + " " + domain1.getName() + " " + domain1.getCd_name() + " (" + domain1.getStart() + "," + domain1.getEnd() + ") " + domain2.getCd_name() + " (" + domain2.getStart() + "," + domain2.getEnd() + ")");
        return false;
    }

    public void domainsInGaps(List<ConservedDomain> list){
        domainsPerPosition = new ArrayList<List<ConservedDomain>>();
        initDomainMap(list);
        for (ConservedDomain domain : list) {
            for(int i = domain.getStart(); i <= domain.getEnd(); i++){
                domainsPerPosition.get(i).add(domain);
            }
        }
    }

    public int howManyDomainsInRangeMax(int gapStart, int gapEnd){
        int i = 0;
        for(int k = gapStart+1; k < gapEnd-1; k++){
            List<ConservedDomain> positionDomains = domainsPerPosition.get(k);
            if(i < positionDomains.size()){
                i = positionDomains.size();
            }
        }
        return i;
    }

    public boolean lessThanHalfInGaps(ConservedDomain domain1, ConservedDomain domain2, int numberOfDomains){
        int amount;
        if(domain1.getEnd() < domain2.getStart()){
            amount = howManyDomainsInRangeMax(domain1.getEnd(), domain2.getStart());
            if(amount > numberOfDomains*0.5){
                writer.writeLine("");
                return false;
            }
        } else {
            amount = howManyDomainsInRangeMax(domain2.getEnd(), domain1.getStart());
            if(amount > numberOfDomains*0.5){
                writer.writeLine("");
                return false;
            }
        }
        return true;
    }

    public void initDomainMap(List<ConservedDomain> list){
        ConservedDomain conservedDomain = list.get(0);
        String name = conservedDomain.getName();
        Protein protein = proteinSequences.get(name);
        int length = 0;
        try {
            length = protein.getLength();
        } catch (NullPointerException ex ){
            ex.printStackTrace();
            System.err.println(name);
        }
        for (int i = 0; i <= length; i++){
            domainsPerPosition.add(new ArrayList<ConservedDomain>());
        }
    }
}
