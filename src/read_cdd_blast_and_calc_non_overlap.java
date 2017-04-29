
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
            cdReader.setFullGeneCD(args[1]);
            timer.elapsedFromLastStamp();
            System.err.println("Seq Reader...");
            cdReader.setSeqReader(args[2]);
            timer.elapsedFromLastStamp();
            System.err.println("InitCDDetectReader...");
            cdReader.initCDDetectReader(args[0]);
            timer.elapsedFromLastStamp();
            System.err.println("Calc Overlap...");
            cdReader.calc_overlap();
            timer.elapsedFromLastStamp();

        }
        else {
            System.err.println("No File!\nUse one Argument for BLAST output as Input\nUse Two Arguments for only CD File + Full Gene File\nUse Three Arguments for CD File + Fullgene Names + Fullgene Sequences(CD Length Comparison)");
        }
    }
}
