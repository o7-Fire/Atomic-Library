import Atom.Math.Array;
import Atom.Translation.GoogleTranslate;
import Atom.Translation.SaddamHusseinTranslator;
import Atom.Utility.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class AnyTest {
    @Test
    public void translation() throws IOException, ExecutionException, InterruptedException {
        GoogleTranslate.main(new String[]{});
        SaddamHusseinTranslator.main(new String[]{});
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
}
