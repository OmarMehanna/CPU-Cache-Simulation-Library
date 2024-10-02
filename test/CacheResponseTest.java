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
    }
}