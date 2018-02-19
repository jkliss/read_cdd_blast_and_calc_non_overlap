
/**
 * Created by students on 07.04.17.
 */

public class read_cdd_blast_and_calc_non_overlap {
    public static void main(String[] args) {
        if(args.length == 1){
            BlastReader breader = new BlastReader(args[0]);
            breader.calc_overlap();
            breader.close_writer();
        } else if(args.length == 2){
            CDDetectReader cdReader = new CDDetectReader();
            cdReader.setFullGeneCD(args[1]);
            cdReader.initCDDetectReader(args[0]);
            cdReader.calc_overlap();
            cdReader.close_writer();
        } else if(args.length == 3){
            ElapsedTimer timer = new ElapsedTimer();
            System.err.println("Detect Reader...");
            CDDetectReader cdReader = new CDDetectReader();
            timer.elapsedFromLastStamp();
            System.err.println("Set FullGeneCD...");
            cdReader.setFullGeneCD(args[1]); // FULL GENE CD FILE - JUST NAMES OF FULL GENE CDs
            timer.elapsedFromLastStamp();
            System.err.println("Seq Reader...");
            cdReader.setSeqReader(args[2]); // SEQUENCES OF CDs in fasta format
            timer.elapsedFromLastStamp();
            System.err.println("InitCDDetectReader...");
            cdReader.initCDDetectReader(args[0]); // CDs detected by bwrps.pl
            timer.elapsedFromLastStamp();
            System.err.println("Calc Overlap...");
            cdReader.calc_overlap();
            timer.elapsedFromLastStamp();
        } else if(args.length == 4){
            ElapsedTimer timer = new ElapsedTimer();
            System.err.println("Detect Reader...");
            CDDetectReader cdReader = new CDDetectReader();
            timer.elapsedFromLastStampIfSignificant();
            System.err.println("Protein sequence Reader...");
            cdReader.setProteinSequences(args[3]); // SEQUENCES OF PROTEINS in fasta format
            timer.elapsedFromLastStampIfSignificant();
            System.err.println("Set FullGeneCD...");
            cdReader.setFullGeneCD(args[1]); // FULL GENE CD FILE - JUST NAMES OF FULL GENE CDs
            timer.elapsedFromLastStampIfSignificant();
            System.err.println("Seq Reader...");
            /**
             * Wenn die CD Sequenzen eingelesen werden, gibt es keine Sequenzen für die Cluster
             * um auch Sequenzlängen für Cluster zu bekommen wurden eine Sequenz im Median aller der zum Cluster gehörenden Sequenzen generiert
             * und der Sequenzdatei angehängt
             */
            cdReader.setSeqReader(args[2]); // SEQUENCES OF CDs in fasta format
            timer.elapsedFromLastStampIfSignificant();
            System.err.println("InitCDDetectReader...");
            cdReader.initCDDetectReader(args[0]); // CDs detected by bwrps.pl
            timer.elapsedFromLastStampIfSignificant();
            System.err.println("Calc Overlap...");
            /**
             * Die Angabe mit 600 aa Residuen ist im Paper Falsch und meint Nukleotide... Daher wird als Schwellenwert 200 aa (also 600/3 angenommen)
             */
            cdReader.removeSmallSequences(200); /** FILTER STEP 6 **/
            cdReader.calc_overlap(true);
            timer.elapsedFromLastStampIfSignificant();
            timer.elapsedFromStart();
        }
        else {
            System.err.println("No File!\nUse one Argument for BLAST output as Input\nUse Two Arguments for only CD File + Full Gene File\nUse Three Arguments for CD File + Fullgene Names + Fullgene Sequences(CD Length Comparison)");
            System.err.println("\nCDD FILE\n" +
                    "FULL GENE FILE\n" +
                    "FASTA FILE CDD SEQUENCES WITH CLUSTERS FOR LENGTH\n" +
                    "FASTA FILE OF ALL SEQUENCES");
        }
    }
}
