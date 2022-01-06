import Atom.Noise.Noise;
import Atom.Noise.PerlinNoiseKemperlin;
import Atom.Noise.PerlinNoisePrime;
import Atom.Noise.PerlinNoiseRiven;
import Atom.Utility.Random;
import org.junit.jupiter.api.Test;

public class NoiseTest {
    public static Noise[] noise = new Noise[]{new PerlinNoisePrime(), new PerlinNoiseRiven(), new PerlinNoiseKemperlin()};
    
    @Test
    public void inRange() {
        for (Noise n : noise) {
            for (int i = 0; i < 1e6; i++) {
                double[] param = new double[]{Random.getDouble(), Random.getDouble(), Random.getDouble()};
                double d2 = n.noise(param[0], param[1]);
                double d3 = n.noise(param[0], param[1], param[2]);
                assert d2 >= -1 && d2 <= 1 :
                        "d2 beyond normal range: " + d2 + " noise: " + n.getClass().getCanonicalName() + " param: " +
                                param[0] + " " + param[1];
                assert d3 >= -1 && d3 <= 1 :
                        "d3 beyond normal range: " + d3 + " noise: " + n.getClass().getCanonicalName() + " param: " +
                                param[0] + " " + param[1] + " " + param[2];
            }
        }
    }
}
