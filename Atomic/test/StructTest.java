import Atom.Struct.ListCapped;
import org.junit.jupiter.api.Test;

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
}
