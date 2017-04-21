/**
 * Created by students on 07.04.17.
 */
public class read_cdd_blast_and_calc_non_overlap {
    public static void main(String[] args) {
        if(args.length == 1){
            BlastReader breader = new BlastReader(args[0]);
            breader.calc_overlap();
        } else if(args.length > 1){
            CDDetectReader cdReader = new CDDetectReader();
            cdReader.setFullGeneCD(args[1]);
            cdReader.initCDDetectReader(args[0]);
            cdReader.calc_overlap();
        }
        else {
            System.err.println("No File!\nUse one Argument for BLAST output as Input\nUse Two Arguments for only CD File + Full Gene File");
        }

    }
}
