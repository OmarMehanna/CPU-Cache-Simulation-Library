/**
 * A structure storing the response from a cache,
 * including the requested data and other possible metrics.
 */
public class CacheResponse {

    private CacheItem data;
    private boolean miss;
    private double time;

    /**
     * Construct a new CacheResponse object
     * @param data the CacheItem requested
     * @param miss boolean whether the requested caused a cache miss
     */
    public CacheResponse(CacheItem data, boolean miss, double time) {
        this.data = data;
        this.miss = miss;
        this.time = time;
    }



    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }


    /**
     * Set boolean for cache miss on this CacheResponse.
     * @param miss boolean for cache miss value
     */
    public void setMiss(boolean miss) {
        this.miss = miss;
    }

    /**
     * Gets whether the request generating this response
     * caused a cache miss or not.
     * @return true if and only if a cache miss occurred.
     */
    public boolean getMiss() {
        return miss;
    }

    /**
     * Get the cache data requested as a CacheItem.
     * @return a CacheItem containing key and data of the request.
     */
    public CacheItem getData() {
        return data;
    }

}
