import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheResponseTest {

    @Test
    void getMiss_False() {
        CacheResponse cr = new CacheResponse(new CacheItem(1,123), false);
        assertFalse(cr.getMiss());
    }

    @Test
    void getMiss_True() {
        CacheResponse cr = new CacheResponse(new CacheItem(1,123), true);
        assertTrue(cr.getMiss());
    }

    @Test
    void setMiss_FalseToTrue() {
        CacheResponse cr = new CacheResponse(new CacheItem(1,123), false);
        cr.setMiss(true);
        assertTrue(cr.getMiss());
    }

    @Test
    void setMiss_TrueToFalse() {
        CacheResponse cr = new CacheResponse(new CacheItem(1,123), true);
        cr.setMiss(false);
        assertFalse(cr.getMiss());
    }

    @Test
    void getData() {
        CacheResponse cr = new CacheResponse(new CacheItem(1,123), true);
        CacheItem test = new CacheItem(1,123);
        CacheItem ci = cr.getData();
        assertEquals(test.getKey(), ci.getKey(), "Keys do not match for a CacheItem in CacheResponse.");
        assertEquals(test.getData(), ci.getData(), "Data does not match for a CacheItem in CacheResponse.");
    }
}