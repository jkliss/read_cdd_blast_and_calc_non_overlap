
/**
 * Created by students on 24.05.17.
 */
public class ExtractSequencesByList {
    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("1. Sequences | 2. List");
        } else {
            SeqReader seqReader = new SeqReader(args[0]);
            Reader reader = new Reader(args[1]);
            String line;
            while((line = reader.readLine()) != null){
                try{
                    if(line.contains(">")){
                        line = line.replace(">", "");
                    }
                    Protein protein = seqReader.get(line);
                    System.out.println(">" + protein.getName() + "\n" + protein.getSeq());
                } catch (NullPointerException ex){
                    System.err.println("Head not found: " + line);
                }
            }
        }
    }
}
