import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class CacheSimTest {

    static String ex1File = "example1.txt";

    void prepareExample1() {
        File fp = new File(ex1File);
        try {
            if (!fp.exists()) {
                fp.createNewFile();
            }
            PrintWriter pw = new PrintWriter(fp);
            for (int i = 0; i < 10; i++) {
                pw.println(Integer.toString(i) + " " + Integer.toString(i));
            }
            pw.close();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void reset() {

    }

    @Test
    void getCacheMisses() {

    }

    @Test
    void cacheToString() {

    }


}