import Atom.Math.Array;
import Atom.Math.Matrix;
import Atom.Math.Meth;
import Atom.Utility.TestingUtility;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class MathTest {
    @Test
    void Meth() throws InvocationTargetException, IllegalAccessException {
        TestingUtility.methodFuzzer(Meth.class.getDeclaredMethods(), true);
        Meth
    }
    
    @Test
    void Matrix() throws InvocationTargetException, IllegalAccessException {
        TestingUtility.methodFuzzer(Matrix.class.getDeclaredMethods(), true);
    }
    
    @Test
    void Array() throws InvocationTargetException, IllegalAccessException {
        TestingUtility.methodFuzzer(Array.class.getDeclaredMethods(), true);
    }
}
