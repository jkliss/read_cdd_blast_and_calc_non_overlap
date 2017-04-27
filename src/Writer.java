import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by students on 26.04.17.
 */
public class Writer {
    BufferedWriter writer;
    File file;

    public Writer(String filename) {
        try {
            file = new File(filename);
            writer = new BufferedWriter(new FileWriter(file), 10000);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Writer could not find/create File");
        }
    }

    public void write(String line){
        try {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not write line: " + line);
        }
    }

    public void writeLine(String line){
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not write line: " + line);
        }
    }

    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Writer could not be closed!");
        }
    }
}
