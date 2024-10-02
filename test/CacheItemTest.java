import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheItemTest {


    @Test
    void getKey() {
        CacheItem ci = new CacheItem(1,123);
        int key = ci.getKey();
        assertEquals(1, key, "Key should be 1");
    }

    @Test
    void getData() {
        CacheItem ci = new CacheItem(1,123);
        int data = ci.getData();
        assertEquals(123, data, "Data should be 123.");
    }

    @Test
    void setData() {
        CacheItem ci = new CacheItem(1,123);
        ci.setData(456);
        int data = ci.getData();
        assertEquals(456, data, "Data should be 456 after set.");
    }

    @Test
    void copy() {
        CacheItem ci = new CacheItem(1,123);
        CacheItem copy = ci.copy();
        assertEquals(ci.getKey(), copy.getKey(), "Key should be copied and equal.");
        assertEquals(ci.getData(), copy.getData(), "Data should be copied and equal");
        ci.setData(456);
        assertNotEquals(ci.getData(), copy.getData(), "Copy should not be modified by changes to original");

    }
}