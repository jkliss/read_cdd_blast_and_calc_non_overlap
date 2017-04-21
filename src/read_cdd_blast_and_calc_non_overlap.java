/**
 * Created by students on 07.04.17.
 */
public class read_cdd_blast_and_calc_non_overlap {
    public static void main(String[] args) {
        if(args.length == 1){
            BlastReader breader = new BlastReader(args[0]);
            breader.calc_overlap();
        } else if(args.length > 1){
            CDDetectReader cdreader = new CDDetectReader(args[0]);
            cdreader.calc_overlap();
        }
        else {
            System.err.println("No File!\nuse -n for only CD File");
        }

    }
}
