import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class CacheTest {

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

    Cache getEmptyCache_Ex1() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        return cache;
    }

    Cache getPartiallyFullCache_Ex1() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        try {
            cache.requestData(1);
            cache.requestData(2);
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
        return cache;
    }

    Cache getFullCache_Ex1() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        try {
            cache.requestData(1);
            cache.requestData(2);
            cache.requestData(3);
            cache.requestData(4);
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
        return cache;
    }

    @Test
    void getCapacity_test1() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        assertEquals(4, cache.getCapacity(), "Cache capacity should be 4.");
    }

    @Test
    void getCapacity_test2() {
        prepareExample1();
        Cache cache = new Cache(128, ex1File);
        assertEquals(128, cache.getCapacity(), "Cache capacity should be 128.");
    }

    @Test
    void reset_empty() {
        Cache cache = getEmptyCache_Ex1();
        cache.reset();
        assertEquals(0, cache.getSize(), "Cache should be empty after reset.");
    }

    @Test
    void reset_partiallyFull() {
        Cache cache = getPartiallyFullCache_Ex1();
        cache.reset();
        assertEquals(0, cache.getSize(), "Cache should be empty after reset.");
    }

    @Test
    void reset_full() {
        Cache cache = getFullCache_Ex1();
        cache.reset();
        assertEquals(0, cache.getSize(), "Cache should be empty after reset.");
    }

    @Test
    void getSize() {

    }

    @Test
    void findData() {

    }

    @Test
    void getRank() {

    }

    @Test
    void evictData() {

    }

    @Test
    void getContents() {

    }


    @Test
    void fetchData() {

    }
}