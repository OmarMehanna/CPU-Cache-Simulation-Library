import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class CacheSimTest {

    static String ex1File = "example1.txt";

    /**
     * Prepare backing store for example 1.
     */
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

    /**
     * reset() black box unit tests
     * 1. reset empty CacheSim
     * 2. reset partially full CacheSim
     * 3. reset full CacheSim
     * 4. reset overfull (more requests than capacitY) CacheSim.
     */

    @Test
    void reset_empty() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.reset();
        assertEquals(0, sim.getCacheMisses(), "Cache misses should equal 0 after reset.");
        assertEquals(0, sim.getCacheHistory().size(), "Cache history should be empty after reset.");
        assertEquals("() () () ()", sim.cacheToString(), "Cache should be empty after reset.");
    }

    @Test
    void reset_partiallyFull() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1, 2});
        sim.reset();
        assertEquals(0, sim.getCacheMisses(), "Cache misses should equal 0 after reset.");
        assertEquals(0, sim.getCacheHistory().size(), "Cache history should be empty after reset.");
        assertEquals("() () () ()", sim.cacheToString(), "Cache should be empty after reset.");
    }

    @Test
    void reset_full() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1, 2, 3, 4});
        sim.reset();
        assertEquals(0, sim.getCacheMisses(), "Cache misses should equal 0 after reset.");
        assertEquals(0, sim.getCacheHistory().size(), "Cache history should be empty after reset.");
        assertEquals("() () () ()", sim.cacheToString(), "Cache should be empty after reset.");
    }

    @Test
    void reset_overfull() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4,1,2,3,4,5,6});
        sim.reset();
        assertEquals(0, sim.getCacheMisses(), "Cache misses should equal 0 after reset.");
        assertEquals(0, sim.getCacheHistory().size(), "Cache history should be empty after reset.");
        assertEquals("() () () ()", sim.cacheToString(), "Cache should be empty after reset.");
    }

    /**
     * getCacheMisses() black box unit tests.
     * 1. empty cache
     * 2. partially full cache
     * 3. full cache
     * 4. Number of requests should equal number of misses (i.e. no cache hits).
     * 5. Number of misses less than the number of requests (i.e. some cache hits)
     */

    @Test
    void getCacheMisses_empty() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        assertEquals(0, sim.getCacheMisses(), "Cache misses should equal 0 to start.");
    }

    @Test
    void getCacheMisses_partiallyFull() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2});
        assertEquals(2, sim.getCacheMisses(), "Cache misses should equal 2 after accessing 2 unique data items.");
    }

    @Test
    void getCacheMisses_full() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4});
        assertEquals(4, sim.getCacheMisses(), "Cache misses should equal 4 after accessing 4 unique data items.");
    }

    @Test
    void getCacheMisses_MissesEqualRequests() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4,5,6});
        assertEquals(6, sim.getCacheMisses(), "Cache misses should equal 6 after accessing unique data items.");
    }

    @Test
    void getCacheMisses_MissesLessRequests() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4,1,2,3,4});
        assertEquals(4, sim.getCacheMisses(), "Cache misses should equal 4 after accessing 4 unique data items.");
    }

    /**
     * cacheToString() black box unit tests.
     * 1. empty cache.
     * 2. partially full cache
     * 3. full cache
     * 4. overfull cache
     * 5. partially full cache with backing store example2
     * 6. full cache with backing store example2
     * 7. overfull cache with backing store example2
     */
    
    @Test
    void cacheToString_empty() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        assertEquals("() () () ()", sim.cacheToString(), "Empty cache should contain nothing.");
    }

    @Test
    void cacheToString_partial() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2});
        assertEquals("(1,1) (2,2) () ()", sim.cacheToString(), "After 2 requests, cache should contain 2 entries.");
    }

    @Test
    void cacheToString_full() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4});
        assertEquals("(1,1) (2,2) (3,3) (4,4)", sim.cacheToString(), "Full cache should contain 4 entries.");
    }

    @Test
    void cacheToString_overfull() {
        prepareExample1();
        CacheSim sim = new CacheSim(4, ex1File);
        sim.simulate(new int[]{1,2,3,4,5,6});
        assertEquals("(5,5) (6,6) (3,3) (4,4)", sim.cacheToString(), "Full cache should contain 4 entries.");
    }






}