import java.util.HashMap;
import java.util.Map;

/**
 * Created by students on 24.05.17.
 */
public class DomainCounter {
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
}
