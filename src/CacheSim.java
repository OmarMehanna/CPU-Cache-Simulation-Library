import java.io.PrintStream;
import java.util.ArrayList;

/**
 * A class to simulate a sequence of requests to a cache
 * and record the effects. It records the cache's contents
 * after each request as well as whether each request resulted
 * in a cache miss or cache hit.
 *
 * This class supports writing out the record to an output stream.
 */
public class CacheSim {

    //the cache object being simulated
    private Cache cache;
    //total number of cache misses
    private int cacheMisses;
    //the sequence of responses received from the cache
    private ArrayList<CacheResponse> responses;
    //the text representation of the simulation record
    private ArrayList<String> simRecord;

    /**
     * Construct a new cache and CacheSim using a
     * particular cache capacity and file path
     * to the cache's backing store.
     * @param capacity the capacity of the cache
     * @param backingStoreFile the file path to the cache's backing store
     */
    public CacheSim(int capacity, String backingStoreFile) {
        cache = new Cache(capacity, backingStoreFile);
        cacheMisses = 0;
        responses = new ArrayList<>();
        simRecord = new ArrayList<>();
    }

    /**
     * Construct a new CacheSim using an existing cache.
     * @param cache the existing cache
     */
    public CacheSim(Cache cache) {
        this.cache = cache;
        cacheMisses = 0;
        responses = new ArrayList<>();
        simRecord = new ArrayList<>();
    }

    /**
     * Simulate a sequence of cache requests given a
     * sequence of keys to request.
     * For each key in keys, starting at index 0,
     * request data from the cache corresponding to that key,
     * recording results along the way.
     * @param keys the sequence of keys
     */
    public void simulate(int[] keys) {
        simRecord.add(this.cacheToString());
        for (int ref : keys) {
            try {
                CacheResponse resp = cache.requestData(ref);
                responses.add(resp);

            } catch (NotFoundException nfe) {
                simRecord.add("FAILURE");
            }
            simRecord.add(this.cacheToString());
        }
    }

    /**
     * Simulate a sequence of cache requests given a
     * sequence of keys and data.
     * For each index i from 0 to keys.size(), either request data or
     * write data to the cache using keys[i] as the key.
     * If data[i] is not null, write that data using keys[i] as the key.
     * Otherwise, simply request the existing data.
     * @param keys the sequence of keys
     * @param data the sequence of data items to write
     */
    public void simulate(ArrayList<Integer> keys, ArrayList<Integer> data) {
        if (keys == null || data == null || keys.size() != data.size()) {
            return;
        }

        simRecord.add(this.cacheToString());
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) == null) {
                continue;
            }

            CacheResponse resp = null;
            try {
                if (data.get(i) == null) {
                    resp = cache.requestData(keys.get(i));
                } else {
                    resp = cache.writeData(keys.get(i), data.get(i));
                }
                responses.add(resp);
            } catch (NotFoundException nfe) {
                simRecord.add("FAILURE");
            }
            simRecord.add(this.cacheToString());
        }
    }

    /**
     * Reset the simulation, including history, current
     * cache state, and any metrics previously recorded.
     */
    public void reset() {
        simRecord.clear();
        cacheMisses = 0;
        responses.clear();
    }

    /**
     * Get the total number of cache misses recorded
     * during the simulation.
     * @return the total number of cache misses
     */
    public int getCacheMisses() {
        int misses = 0;
        for (CacheResponse resp : responses) {
            if(resp.getMiss()) {
                misses++;
            }
        }
        return misses;
    }

    /**
     * Get a record of the simulation's history as
     * a list of Strings.
     * In the returned list, index 0 is the initial state
     * of the cache as a String and index i is the state
     * of the cache after the i'th request.
     * For requested keys which are not found, the corresponding
     * entry in the history is "FAILURE" rather than
     * the cache's state after that request.
     * @see cacheToString
     * @return the cache's history as a
     */
    public ArrayList<String> getCacheHistory() {
        ArrayList<String> history = new ArrayList<>();
        for (String rec : simRecord) {
            history.add(rec);
        }
        return history;
    }

    /**
     * Get a string representation of underlying cache's
     * current contents.
     * Each index in the cache is represented as a comma-separated
     * pair of items inside parentheses: "(key,data)".
     * Each index in the string is separated by a blank space.
     * If the index in the cache does not contain any data,
     * that index is represented as empty parentheses: "()".
     * @return the string representation of the cache
     */
    public String cacheToString() {
        ArrayList<CacheItem> cacheItems = cache.getContents();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cacheItems.size(); i++) {
            CacheItem ci = cacheItems.get(i);
            String item;
            if (ci == null) {
                item = "()";
            } else {
                item = String.format("(%d,%d)", ci.getKey(), ci.getKey());
            }

            sb.append(item);
            if (i < cacheItems.size()-1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }


}
