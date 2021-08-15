import Atom.Reflect.UnThread;
import Atom.String.WordGenerator;
import Atom.Struct.FunctionalPoolObject;
import Atom.Struct.History;
import Atom.Struct.ListCapped;
import Atom.Struct.Queue;
import Atom.Utility.TestingUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class StructTest {
    @Test
    void ArrayListCappedR() {
        List<Integer> list = new ListCapped<>(10, true);
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        assert list.size() == 5 : list.size();
        for (int i = 5; i < 15; i++) {
            list.add(i);
        }
        assert list.size() == 10 : list.size();
        assert list.get(9) == 9 : list;
    }
    
    @Test
    void History() {
        History<Integer> first = new History<>(1), second, third;
        UnThread.sleep(10);
        second = first.future(2);
        assert first.getFuture() == second;
        assert second.getPast() == first;
        UnThread.sleep(10);
        third = second.future(3);
        System.out.println(second.asTime().elapsedS(third.asTime()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jason;
        System.out.println(gson.toJson(first));
        System.out.println("-------------------------");
        System.out.println(jason = gson.toJson(second));
        System.out.println("-------------------------");
        System.out.println(gson.toJson(third));
        second = gson.fromJson(jason, History.class);
        System.out.println(second);
    }
    
    @Test
    void ArrayListCapped() {
        List<Integer> list = new ListCapped<>(10);
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        assert list.size() == 5 : list.size();
        for (int i = 5; i < 15; i++) {
            list.add(i);
        }
        assert list.size() == 10 : list.size();
        assert list.get(9) == 14 : list;
        
    }
    
    @Test
    void Queue() {
        Queue<String> queue = new Queue<>();
        queue.add(WordGenerator.randomString());
        queue.addLast(WordGenerator.randomString());
        queue.addFirst(WordGenerator.randomString());
        assert queue.size == 3;
        queue.removeFirst();
        queue.removeLast();
        queue.removeIndex(0);
        queue.add(WordGenerator.randomString());
        try {
            TestingUtility.methodFuzzer(queue, queue.getClass().getDeclaredMethods(), true);
        }catch(InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
    
    @Test
    void PoolObject() {
        FunctionalPoolObject.main(new String[]{});
        
    }
}
