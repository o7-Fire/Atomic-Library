import Atom.File.FileUtility;
import Atom.Math.Array;
import Atom.Utility.Meta.TestingUtility;
import Atom.Utility.Notify;
import Atom.Utility.Random;
import Atom.Utility.Utility;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class UtilityTest {
    @Test
    public void StringUtility() {
        assert Utility.repeatThisString("\n", 10).equals("\n\n\n\n\n\n\n\n\n\n");
        assert Utility.capitalizeEnforce("saDdAm").equals("Saddam");
        assert Utility.capitalizeEnforce("Saddam").equals("Saddam");
        assert Utility.containIntOnly("1234");
        assert Utility.containIntOnly("58726758265827652867528765823659173912379127319741094129481294141241");
        assert !Utility.containIntOnly("fafahf08r3208h293b19-b1");
        assert Utility.capitalizeFirstLetter("saddaM").equals("SaddaM");
        assert Utility.capitalizeFirstLetter("SaddaM").equals("SaddaM");
        assert Utility.lowerFirstLetter("SaddaN").equals("saddaN");
        assert Utility.lowerFirstLetter("saddaNN").equals("saddaNN");
        assert Utility.shrinkChar("aaaaabbbbbcccccccccc").equals("abc");
        assert Utility.capitalizeTitle("saddam hussein").equals("Saddam Hussein");
        assert Utility.capitalizeTitle("saDdAm husseIN").equals("SaDdAm HusseIN");
        assert Utility.capitalizeTitle("saDdAm husseIN", true).equals("Saddam Hussein");
        assert Arrays.equals(Utility.sliceInt("111111,2222222,333333,4444444", ","), new int[]{111111, 2222222, 333333, 4444444});
        assert !Utility.shuffle("aaabbbccc").equals("aaabbbccc");
    }
    
    @Test
    void randomUtility() throws InvocationTargetException, IllegalAccessException {
        assert Random.getInt() != Random.getInt();
        assert Random.getInt(-100, 0) != Random.getInt(0, 100);
        assert Random.getInt(100) != Random.getInt();
        assert Random.getDouble(100) > 0;
        assert Random.getDouble(-100, 100) != Random.getDouble(100);
        assert Random.getLong() != Random.getLong();
        assert Random.getLong(100) != Random.getLong();
        assert Random.getLong(-200, 100) != Random.getLong();
        assert Random.getFloat(100) > 1;
        assert Random.getFloat(-100, 100) != Random.getFloat();
        Random.main(new String[]{});
        TestingUtility.methodFuzzer(Random.class.getDeclaredMethods(), true);
    }

    @Test
    public void AnyUtility() {
        Notify.main(new String[]{});
    }

    @Test
    public void FileUtility() throws IOException {
        File test = FileUtility.temp(), test2 = FileUtility.temp(), test3 = FileUtility.temp();
        //start
        assert FileUtility.getCurrentWorkingDir().exists();
        assert test.exists() == false;
        assert test2.exists() == false;
        assert test3.exists() == false;
        assert test.equals(test2) == false;
        assert test.equals(test3) == false;
        int length1 = Random.getInt(10, 1200), length2 = Random.getInt(2, 10);
        
        //extension
        assert FileUtility.getExtension(test).equals("temp");
        assert FileUtility.getNameWithoutExtension("assad.1984.temp").equals("assad.1984");
        
        //write random
        byte[] b1 = new byte[length1], b2 = new byte[length2];
        Array.random(b1);
        Array.random(b2);
        assert Arrays.equals(b1, b2) == false;
        assert FileUtility.write(test, b1);
        assert FileUtility.write(test3, b2);
        
        //assert
        assert test.exists();
        byte[] br1, brr1, br2, br3, brr3;
        br1 = FileUtility.readAllBytes(test);//temp1 b1
        br3 = FileUtility.readAllBytes(test3);
        assert Arrays.equals(br1, b1);
        assert Arrays.equals(br3, b2);
        
        //copy
        assert FileUtility.copy(test, test2);
        assert FileUtility.copy(test, test2) == false;
        assert test.exists();
        assert test2.exists();
        brr1 = FileUtility.readAllBytes(test2);
        assert Arrays.equals(b1, brr1);
        
        //replace
        assert FileUtility.replace(test, test3);
        brr3 = FileUtility.readAllBytes(test3);
        assert Arrays.equals(b1, brr3);
        
        //append
        assert FileUtility.write(test, b2, true);
        br2 = FileUtility.readAllBytes(test);
        assert br2.length == (b1.length + b2.length);
        
        
        //end
        assert test.delete();
        assert test.exists() == false;
    }
}
