import Atom.Math.Array;
import Atom.Reflect.Reflect;
import Atom.Translation.GoogleTranslate;
import Atom.Utility.Utility;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnyTest {
    @Test
    public void translation() {
        try {
            String result = new GoogleTranslate().translate("what", "en", "fr");
            assert result != null : "Translation should not be null";
            System.out.println(result);
        } catch (IOException e) {
            // Google's public endpoint rate-limits clients with HTTP 429; the library cannot control that.
            Assumptions.assumeTrue(false, "Google Translate unavailable: " + e.getMessage());
        }
    }
    
    
    @Test
    public void matrixFlattening() {
        int[][] matrix = new int[][]{new int[]{1}, new int[]{2, 2}, new int[]{3, 3, 3}, new int[]{4, 4, 4, 4}};
        System.out.println(Array.visualize(matrix));
        System.out.println("Flattening");
        int[] vector = Utility.flattenMatrix(matrix);
        int[] real = new int[]{1, 2, 2, 3, 3, 3, 4, 4, 4, 4};
        System.out.println(Arrays.toString(vector));
        assert Arrays.equals(vector, real) : "should be " + Arrays.toString(real);
    }
    
    @Test
    public void tensorRandomVisualize() {
        System.out.println();
        boolean[][][] tensor = new boolean[3][4][5];
        Array.random(tensor);
        short[][][] shorts = new short[3][4][5];
        Array.random(shorts);
        System.out.println(Array.visualize(tensor));
        System.out.println();
        System.out.println(Arrays.deepToString(tensor));
    }
    
    @Test
    public void reflectionUtilities() {
        ClassLoader loader = AnyTest.class.getClassLoader();
        assertTrue(Arrays.stream(Reflect.getDefinedPackages(loader)).anyMatch(p -> p.getName().startsWith("Atom.")),
                "getDefinedPackages should include library packages");
        assertTrue(!Reflect.findClasses("Atom.Math", loader).isEmpty(), "findClasses should discover library classes");
    }
}
