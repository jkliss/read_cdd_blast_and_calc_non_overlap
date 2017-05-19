import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by students on 19.05.17.
 */
public class Reader {
    BufferedReader reader;
    File file;

    public Reader(String filename) {
        try {
            file = new File(filename);
            reader = new BufferedReader(new FileReader(file), 10000);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Writer could not find/create File");
        }
    }

    public String readLine(){
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public void close(){
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Writer could not be closed!");
        }
    }

    public void reset(){
        try {
            reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List readAllLines(){
        List<String> list = new ArrayList<String>();
        String line;
        while((line = readLine()) != null){
            list.add(line);
        }
        if(list.size() == 0){
            System.err.println("empy file: " + file.getName());
        }
        return list;
    }
}
