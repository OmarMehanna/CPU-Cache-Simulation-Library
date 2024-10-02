/**
 * An item to be stored in a Cache,
 * grouping an integer key with an integer
 * piece of data.
 */
public class CacheItem {

    private int key;
    private int data;

    /**
     * Create a new empty CacheItem.
     */
    public CacheItem() {
        this.key = -1;
        this.data = 0;
    }

    /**
     * Create a new CacheItem with key and data.
     * @param key the key for the created CacheItem
     * @param data the data for the created CacheItem
     */
    public CacheItem(int key, int data) {
        this.key = key;
        this.data = data;
    }

    /**
     * Get the CacheItem's key.
     * @return the integer key
     */
    public int getKey() {
        return key;
    }

    /**
     * Get the CacheItem's data.
     * @return the integer data
     */
    public int getData() {
        return data;
    }

    /**
     * Set the data for this CacheItem.
     * @param data the integer data value.
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * Get a copy of this CacheItem with same key and data.
     * @return a new CacheItem with same values.
     */
    public CacheItem copy() {
        return new CacheItem(this.key, this.data);
    }
}
