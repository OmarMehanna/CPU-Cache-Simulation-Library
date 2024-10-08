import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class CacheTest {

    static String ex1File = "example1.txt";

    /**
     * Prepare the backing store file for example1.
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
     * Get an empty cache with backing store ex1File.
     */
    Cache getEmptyCache_Ex1() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        return cache;
    }

    /**
     * Get a cache with capacity 4, backing store ex1File, and
     * two previously accessed keys: 1 and 2.
     */
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

    /**
     * Get a cache with capacity 4, backing store ex2File, and
     * four previously accessed keys: 1, 2, 3, and 4.
     */
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

    /**
     * getCapacity() black box unit tests.
     * 1. "small" capacity: 4
     * 2. "large" capacity: 128
     */

    @Test
    void getCapacity_commonSmall() {
        prepareExample1();
        Cache cache = new Cache(4, ex1File);
        assertEquals(4, cache.getCapacity(), "Cache capacity should be 4.");
    }

    @Test
    void getCapacity_commonLarge() {
        prepareExample1();
        Cache cache = new Cache(128, ex1File);
        assertEquals(128, cache.getCapacity(), "Cache capacity should be 128.");
    }

    /**
     * getSize() black box unit tests.
     * 1. An empty cache
     * 2. A partially full cache
     * 3. A full cache.
     * 4. An overfull (more requests than capacity) cache.
     */

    @Test
    void getSize_empty() {
        Cache cache = getEmptyCache_Ex1();
        assertEquals(0, cache.getSize(), "Empty cache should have size 0.");
    }

    @Test
    void getSize_partiallyFull() {
        Cache cache = getPartiallyFullCache_Ex1();
        assertEquals(2, cache.getSize(), "Partially full cache should have size 2.");
    }

    @Test
    void getSize_full() {
        Cache cache = getFullCache_Ex1();
        assertEquals(cache.getCapacity(), cache.getSize(), "Full cache should have size equal to capacity.");
    }

    @Test
    void getSize_overfull() {
        Cache cache = getFullCache_Ex1();
        //request extra data
        try {
            cache.requestData(5);
            cache.requestData(6);
        } catch (NotFoundException nfe) {
            fail("Data was not found in the backing store but should be.");
        }
        assertEquals(cache.getCapacity(), cache.getSize(), "Full cache should have size equal to capacity.");
    }

    /**
     * getRank() black box unit tests.
     * 1. Requested key does not exist in an empty cache.
     * 2. Requested key does not exist in a full cache.
     * 3. Get rank of key that was just accessed.
     * 4. Get rank of a previously accessed key
     * 5. Attempt to get rank of a CacheItem recently evicted.
     */

    @Test
    void getRank_notFoundEmpty() {
        Cache cache = getEmptyCache_Ex1();
        assertEquals(-1, cache.getRank(1), "If key is not in cache, -1 rank expected.");
    }

    @Test
    void getRank_notFoundFull() {
        Cache cache = getFullCache_Ex1();
        assertEquals(-1, cache.getRank(10), "If key is not in cache, -1 rank expected.");
    }

    @Test
    void getRank_justAccessed() {
        Cache cache = getFullCache_Ex1();
        try {
            cache.requestData(6);
            int rank = cache.getRank(6);
            assertEquals(0, rank, "Rank of most recently accessed key should be 0.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    @Test
    void getRank_older() {
        Cache cache = getFullCache_Ex1();
        try {
            cache.requestData(6);
            int rank = cache.getRank(3);
            assertTrue(rank > 0, "Rank of previously accessed key should be greater than 0.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    @Test
    void getRank_evicted() {
        Cache cache = getFullCache_Ex1();
        try {
            cache.requestData(5);
            cache.requestData(6);
            cache.requestData(7);
            cache.requestData(8);
            assertEquals(-1, cache.getRank(1), "If key is not in cache, -1 rank expected.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    /**
     * findData() black box unit tests.
     * 1. Try to find data in an empty cache.
     * 2. Find data that should exist in a partially filled cache
     * 3. Try to find data that shouldn't exist in a partially filled cache
     * 4. Find data that should exist in a filled cache
     * 5. Try to find data that shouldn't exist in a filled cache
     */

    @Test
    void findData_empty() {
        Cache cache = getEmptyCache_Ex1();
        int idx = cache.findData(1);
        assertEquals(-1, idx, "Empty cache should have no data to find.");
    }

    @Test
    void findData_partiallyFullExists() {
        Cache cache = getPartiallyFullCache_Ex1();
        int idx = cache.findData(1);
        assertEquals(0, idx, "Cache should install first requested data in index 0.");
    }

    @Test
    void findData_partiallyFullNotExists() {
        Cache cache = getPartiallyFullCache_Ex1();
        int idx = cache.findData(6);
        assertEquals(-1, idx, "Index should be -1 for data not in cache.");
    }

    @Test
    void findData_fullExists() {
        Cache cache = getFullCache_Ex1();
        int idx = cache.findData(1);
        assertEquals(0, idx, "Cache should install first requested data in index 0.");
    }

    @Test
    void findData_fullNotExists() {
        Cache cache = getFullCache_Ex1();
        int idx = cache.findData(6);
        assertEquals(-1, idx, "Index should be -1 for data not in cache.");
    }

    /**
     * reset() black box unit tests.
     * 1. reset an empty cache
     * 2. reset a partially fully cache
     * 3. reset a full cache
     */

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

    /**
     * evictData() black box unit tests
     * 1. Try to evict data from an empty cache.
     * 2. Try to evict data from a partially full cache to install a new item.
     * 3. Try to evict data from a partially full cache to install an extant item.
     * 4. Evict data from a full cache to install a new item.
     * 5. Try to evict data from a full cache to install an extant item.
     */
    @Test
    void evictData_empty() {
        Cache cache = getEmptyCache_Ex1();
        int idx = cache.evictData(4);
        assertEquals(-1, idx, "evictData should return -1 index for a non-full cache.");
    }

    @Test
    void evictData_partiallyFullNew() {
        Cache cache = getPartiallyFullCache_Ex1();
        int idx = cache.evictData(4);
        assertEquals(-1, idx,"evictData should return -1 index for a non-full cache.");
    }

    @Test
    void evictData_partiallyFullExists() {
        Cache cache = getPartiallyFullCache_Ex1();
        int idx = cache.evictData(1);
        assertEquals(-1, idx,"evictData should return -1 index for key already in cache.");
    }

    @Test
    void evictData_FullNew() {
        Cache cache = getFullCache_Ex1();
        int idx = cache.evictData(6);
        assertEquals(0, idx, "evictData should return LRU index (0) for eviction candidate.");
        assertEquals(3, cache.getSize(), "Cache size should be reduced by an eviction");
    }

    @Test
    void evictData_FullExists() {
        Cache cache = getFullCache_Ex1();
        int idx = cache.evictData(4);
        assertEquals(-1, idx,"evictData should return -1 index for key already in cache.");
    }

    /**
     * getContents() black box tests
     * 1. Empty cache
     * 2. Partially full cache
     * 3. Full cache.
     * 4. Overfull (more requests than capacity) cache.
     */

    @Test
    void getContents_empty() {
        Cache cache = getEmptyCache_Ex1();
        ArrayList<CacheItem> contents = cache.getContents();
        for (CacheItem ci : contents) {
            assertNull(ci, "Empty cache should return null CacheItems");
        }
    }

    @Test
    void getContents_partiallyFull() {
        Cache cache = getPartiallyFullCache_Ex1();
        ArrayList<CacheItem> contents = cache.getContents();
        assertEquals(4, contents.size(), "Cache contents should be size 4");
        assertEquals(1, contents.get(0).getKey(), "First index should have CacheItem 1");
        assertEquals(2, contents.get(1).getKey(), "First index should have CacheItem 2");
        assertNull(contents.get(2));
        assertNull(contents.get(3));
    }

    @Test
    void getContents_full() {
        Cache cache = getFullCache_Ex1();
        ArrayList<CacheItem> contents = cache.getContents();
        assertEquals(4, contents.size(), "Cache contents should be size 4");
        assertEquals(1, contents.get(0).getKey(), "First index should have CacheItem 1");
        assertEquals(2, contents.get(1).getKey(), "Second index should have CacheItem 2");
        assertEquals(3, contents.get(2).getKey(), "Third index should have CacheItem 3");
        assertEquals(4, contents.get(3).getKey(), "Fourth index should have CacheItem 4");
    }

    @Test
    void getContents_overfull() {
        Cache cache = getFullCache_Ex1();
        try {
            cache.requestData(5);
            cache.requestData(6);
            ArrayList<CacheItem> contents = cache.getContents();
            assertEquals(4, contents.size(), "Cache contents should be size 4");
            assertEquals(5, contents.get(0).getKey(), "First index should have CacheItem 5");
            assertEquals(6, contents.get(1).getKey(), "Second index should have CacheItem 6");
            assertEquals(3, contents.get(2).getKey(), "Third index should have CacheItem 3");
            assertEquals(4, contents.get(3).getKey(), "Fourth index should have CacheItem 4");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }


    /**
     * fetchData() black box tests.
     * 1. First entry from the backing store.
     * 2. Last entry from the backing store.
     * 3. Try to fetch data that does not exist
     * 4. Fetch data from backing store.
     */

    @Test
    void fetchData_firstEntry() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            int data = cache.fetchData(0);
            assertEquals(0, data, "Example 1 key 0 should have data 0.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    @Test
    void fetchData_lastEntry() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            int data = cache.fetchData(9);
            assertEquals(9, data, "Example 1 key 9 should have data 9.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    @Test
    void fetchData_notExist() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            int data = cache.fetchData(1243);
            fail();
        } catch (NotFoundException e) {
            //Intended outcome! Pass!
        }
    }

    @Test
    void fetchData_Exists() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            int data = cache.fetchData(5);
            assertEquals(5, data, "Example 1 key 5 should have data 5.");
        } catch (NotFoundException e) {
            fail("Data was not found in the backing store but should be.");
        }
    }

    /**
     * pushData() black box tests.
     * 1. First entry from the backing store.
     * 2. Last entry from the backing store.
     * 3. Try to fetch data that does not exist
     * 4. Fetch data from backing store.
     */

    @Test
    void pushData_firstEntry() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            cache.pushData(0, 123);
            assertEquals(123, cache.fetchData(0), "Data was updated in the backing store.");
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    void pushData_lastEntry() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            cache.pushData(9, 123);
            assertEquals(123, cache.fetchData(9), "Data was updated in the backing store.");
        } catch (NotFoundException e) {
            fail();
        }
    }

    @Test
    void pushData_notExist() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            cache.pushData(1243, 789);
            fail();
        } catch (NotFoundException e) {
            //Intended outcome! Pass!
        }
    }

    @Test
    void pushData_Exists() {
        try {
            prepareExample1();
            Cache cache = new Cache(4, ex1File);
            cache.pushData(1, 123);
            assertEquals(123, cache.fetchData(1), "Data was updated in the backing store.");
        } catch (NotFoundException e) {
            fail();
        }
    }

}