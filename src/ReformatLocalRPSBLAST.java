import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by students on 19.06.17.
 */
public class ReformatLocalRPSBLAST {
    HashMap<String, String> translator = new HashMap<String, String>(1000000);
    HashMap<String, String> translatorShort = new HashMap<String, String>(1000000);

    public static void main(String[] args) {
        ReformatLocalRPSBLAST reformatLocalRPSBLAST = new ReformatLocalRPSBLAST();
        reformatLocalRPSBLAST.readIdList();
        reformatLocalRPSBLAST.readCDList(args[0]);

    }

    public void readCDList(String CDFile){
        File file = new File(CDFile);
        if(!file.exists()){
            System.err.println(CDFile + " not found in Directory");
        } else {
            Writer writer = new Writer(CDFile + ".format_out");
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    if(! sCurrentLine.contains("#")){
                        String[] split = sCurrentLine.split("\\s+");
                        String cd_number = split[1].replace("CDD:", "");
                        try {
                            String cd_name = translator.get(cd_number);
                            String cd_short = translatorShort.get(cd_number);
                            // LINE NOCH ANPASSEN AN WEB OUTPUT
                            String printLine = split[0] + "\tlocal_non_spec\t" + cd_number + "\t" + split[6] + "\t" + split[7] + "\t" + split[10] + "\t" + split[2] + "\t" + cd_name + "\t" + cd_short;
                            writer.writeLine(printLine);
                        } catch (NullPointerException ex){
                            System.err.println(cd_number +  " translation not found!");
                            System.out.println(cd_number + " translation not found!");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            writer.flush();
        }
    }


    public void readIdList(){
        File file = new File("cddid_all.tbl");
        if(!file.exists()){
            System.err.println("cddid_all.tbl not found in Directory");
        } else {
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] split = sCurrentLine.split("\t");
                    translator.put(split[0], split[1]);
                    translatorShort.put(split[0], split[2]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                    if (fr != null)
                        fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
