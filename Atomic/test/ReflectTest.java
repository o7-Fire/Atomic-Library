import Atom.Reflect.Reflect;
import org.junit.jupiter.api.Test;

public class ReflectTest {
    @Test
    void Reflect() {
        int[][][][][] e = new int[2][2][3][4][5];
        int[][][][][] res2 = (int[][][][][]) Reflect.getRandomPrimitiveArray(e.getClass());
        int[][][][][] res1 = (int[][][][][]) Reflect.getRandomPrimitive(e.getClass());
    }
}
