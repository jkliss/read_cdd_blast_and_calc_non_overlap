import java.util.HashMap;
import java.util.Map;

/**
 * Created by students on 24.05.17.
 */
public class DomainCounter {
    Writer writer = new Writer("DomainCounter.out");
    Map<String, Integer> domainCount = new HashMap<String, Integer>();

    public void inc(String name){
        if(!domainCount.containsKey(name)){
            domainCount.put(name, 1);
        } else {
            domainCount.put(name, domainCount.get(name)+1);
        }
    }

    public int getCount(String name){
        return domainCount.get(name);
    }

    public void addCombination(String name1, String name2){
        inc(name1 + name2);
        inc(name2 + name1);
    }

    public int getCountCombination(String name1, String name2){
        if(getCount(name1 + name2) < getCount(name2 + name1)){
            return getCount(name2 + name1);
        } else {
            return getCount(name1 + name2);
        }
    }

    public void printAllCounts(){
        for (String domain_combinations : domainCount.keySet()) {
            writer.writeLine(domain_combinations + "\t" + domainCount.get(domain_combinations));
        }
        writer.flush();
    }
}
