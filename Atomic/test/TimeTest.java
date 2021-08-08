import Atom.Reflect.UnThread;
import Atom.Time.Time;
import Atom.Time.Timer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class TimeTest {
    @Test
    void time() {
        Timer timer = new Timer(TimeUnit.SECONDS, 4);
        Time time = new Time(TimeUnit.SECONDS, 5);
        Time millisTime = time.convert(TimeUnit.MILLISECONDS);
        assert millisTime.getSrc() == 5000;
        UnThread.sleep(millisTime.getSrc() + 20);
        assert timer.get();
        
    }
}
