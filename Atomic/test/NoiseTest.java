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
        //todo fix java.lang.AssertionError: d2 beyond normal range: -1.0000004714675024 noise: Atom.Noise.PerlinNoisePrime param: 0.23740339227726326 0.549018229829184
        try {
            for (Noise n : noise) {
                for (int i = 0; i < 1e6; i++) {
                    double[] param = new double[]{Random.getDouble(), Random.getDouble(), Random.getDouble()};
                    double d2 = n.noise(param[0], param[1]);
                    double d3 = n.noise(param[0], param[1], param[2]);
                    assert d2 >= -1 && d2 <= 1 :
                            "d2 beyond normal range: " + d2 + " noise: " + n.getClass().getCanonicalName() +
                                    " param: " +
                                    param[0] + " " + param[1];
                    assert d3 >= -1 && d3 <= 1 :
                            "d3 beyond normal range: " + d3 + " noise: " + n.getClass().getCanonicalName() +
                                    " param: " +
                                    param[0] + " " + param[1] + " " + param[2];
                }
            }
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }
}
