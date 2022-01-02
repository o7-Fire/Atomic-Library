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
            for (int i = 0; i < Random.getInt(200); i++) {
                double d2 = n.noise(Random.getNextDouble(), Random.getNextDouble(), Random.getNextDouble());
                double d3 = n.noise(Random.getNextDouble(), Random.getNextDouble());
                assert d2 >= -1 && d2 <= 1 : "d2 beyond normal range: " + d2;
                assert d3 >= -1 && d3 <= 1 : "d3 beyond normal range: " + d3;
            }
        }
    }
}
