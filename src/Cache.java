import java.io.*;
import java.util.ArrayList;

/**
 * A data structure for caching content for easier access
 * from a backing store. Data in the cache is accessible
 * as a CacheItem: key-data pairs of integers.
 * Keys must be non-negative.
 *
 * Users request data by providing the data's associated key.
 * Recently requested data is stored in the cache until
 * the cache is full and new requests displace older data.
 * This cache uses a least-recently-used replacement policy:
 * when new data is requested and installed in the cache,
 * the piece of data accessed furthest in the past
 * is replaced.
 *
 * Data in the backing store file is stored as one piece of data
 * per line, with each line containing the data element's key,
 * a blank space, and then the data element's actual data.
 *
 */
public class Cache {

    protected CacheItem[] data;
    protected int[] rank;

    protected int size;
    protected int capacity;

    protected BackingStore backingStore;

    /**
     * Create a new Cache with a positive capacity and backing store
     * as a file located at filePath.
     *
     * @param capacity a positive integer capacity of the cache
     * @param filePath the file path to the file acting as backing store.
     */
    public Cache(int capacity, String filePath) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Cache capacity must be positive.");
        }
        data = new CacheItem[capacity];
        size = 0;
        this.capacity = capacity;
        rank = new int[capacity];
        this.backingStore = new BackingStore(filePath);
    }

    /**
     * Get the capacity of the cache, the number of
     * CacheItems it can store at one time.
     *
     * @return the capacity of the cache
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Get the current number of CacheItems stored in the cache.
     *
     * @return the number of items
     */
    public int getSize() {
        return size;
    }

    /**
     * Reset the cache and clear its contents.
     * Results in a cache state as if it was newly constructed.
     */
    public void reset() {
        this.size = 0;
        rank = new int[capacity];
        data = new CacheItem[capacity];
    }

    /**
     * Given a key, find the location of the associated data
     * within the cache as an index.
     * @param key the key to search for
     * @return the index of the associated data in the cache
     */
    public int findData(int key) {
        for (int i = 0; i < capacity; i++) {
            if (this.data[i] != null && this.data[i].getKey() == key) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Request data from the cache given the data's associated key.
     * If the key exists in the cache or backing store, returns the
     * data associated with it wrapped as a CacheResponse.
     * If the key is not found in the cache or backing store,
     * throw a NotFoundException.
     *
     * Whenever data is requested in this way, that same data
     * will be always be available in the cache during the next request.
     * This guarantee does not extend beyond the immediate next request.
     * @see CacheResponse
     *
     * @param key the key for the requested data
     * @return a CacheResponse object holding the requested key-data pair
     * @throws NotFoundException if the requested key is not found
     */
    public CacheResponse requestData(int key) throws NotFoundException {
        int foundIndex = findData(key);
        if (foundIndex < 0) {
            int val = backingStore.fetchData(key);
            installData(key, val);
            //use a recursive call to find the data after installation
            CacheResponse ret = requestData(key);
            ret.setMiss(true);
            return ret;
        }

        updateRanks(foundIndex);
        CacheItem retItem = data[foundIndex].copy();
        return new CacheResponse(retItem, false);
    }

    /**
     * Write an updated data value for a particular key.
     * If the given key is not already present in the cache,
     * it first requests this key from the backing store
     * and installs it in the cache.
     * With the key in the cache, it then updates the associated
     * data to equal newData locally within the cache
     * and writes the updated value to the backing store.
     * Writing data thus triggers a data access
     * and affects rank.
     *
     * If the key exists in the backing store, returns it and the
     * newly updated data associated with it wrapped as a CacheResponse.
     * If not, a NotFoundException is thrown.
     *
     * @see Cache#getRank
     * @see CacheResponse
     *
     * @param key the key whose data is to be updated
     * @param newData the new data to write
     * @return a CacheResponse with updated key-data pair
     * @throws NotFoundException if the provided key does not exist in the backing store
     */
    public CacheResponse writeData(int key, int newData) throws NotFoundException {
        int idx = findData(key);
        boolean miss = false;
        if (idx < 0) {
            int data = backingStore.fetchData(key);
            idx = installData(key, data);
            miss = true;
        }
        backingStore.pushData(key, newData);
        this.data[idx].setData(newData);

        updateRanks(idx);
        CacheResponse ret = new CacheResponse(this.data[idx].copy(), miss);
        return ret;
    }


    /**
     * Get the rank of the CacheItem with the specified key.
     * Rank encodes the relative access history of CacheItems.
     * A rank of 0 is the most recently accessed item,
     * with larger ranks encoding accesses further in the past.
     * Data can be accessed by either a read (i.e. requestData())
     * or a write (i.e. writeData())
     *
     *
     * If the requested key does not exist in the cache,
     * return a special value of -1.
     *
     * @param key the key of the CacheItem whose rank is to be retrieved
     * @return the rank of the CacheItem with the associated key, or -1 if not found.
     */
    public int getRank(int key) {
        int idx = findData(key);
        if (idx < 0) {
            return -1;
        }
        return rank[idx];
    }

    /**
     * Update ranks of CacheItems in the cache given that
     * the item with the specified index was just accessed.
     * @param index the index of the CacheItem just accessed.
     */
    protected void updateRanks(int index) {
        rank[index] = 0;

        for (int i = 0; i < index; i++) {
            if (this.data[i] != null) {
                this.rank[i] += 1;
            }
        }
        for (int i = index + 1; i < this.capacity; i++) {
            if (this.data[i] != null) {
                this.rank[i] += 1;
            }
        }

    }

//    /**
//     * Fetch data from the backing store given the associated key.
//     * This method only fetches the data and returns it;
//     * it does not interact with the cache at all.
//     * If the key is not found in the backing store, throw
//     * a NotFoundException.
//     *
//     * @param key the key of the requested data item
//     * @return the data from the backing store with requested key if found
//     * @throws NotFoundException if the requested key is not found
//     */
//    public int fetchData(int key) throws NotFoundException {
//        int data = 0;
//
//        boolean found = false;
//        String cmpKey = Integer.toString(key);
//
//        try {
//            BufferedReader input = new BufferedReader(new FileReader(this.backingStore));
//            String line;
//            while (!found && (line = input.readLine()) != null) {
//                String[] vals = line.split(" ");
//                if (vals.length == 2 && vals[0].equals(cmpKey)) {
//                    data = Integer.parseInt(vals[1]);
//                    found = true;
//                }
//            }
//
//            input.close();
//
//        } catch (NumberFormatException | IOException nfe) {
//            found = false;
//        }
//
//        if (!found) {
//            throw new NotFoundException();
//        }
//        return data;
//    }

//    /**
//     * Write data to the backing store.
//     * Given integers of a key and newData, try to update
//     * the backing store by finding the key and changing
//     * the associated data to newData.
//     * The order of keys in the backing store does not change.
//     * If the key is not found in the backing store, throws
//     * a NotFoundException.
//     * @param key the key of the data item to update
//     * @param newData the new data to write
//     * @throws NotFoundException if the specified key is not found
//     */
//    public void pushData(int key, int newData) throws NotFoundException {
//        boolean found = false;
//        String cmpKey = Integer.toString(key);
//        StringBuilder sb = new StringBuilder();
//        try {
//            BufferedReader input = new BufferedReader(new FileReader(this.backingStore));
//            String line;
//            while ((line = input.readLine()) != null) {
//                String[] vals = line.split(" ");
//                if (vals.length == 2 && vals[0].equals(cmpKey)) {
//                    found = true;
//                    sb.append(vals[0]);
//                    sb.append(" ");
//                    sb.append(newData);
//                    sb.append("\n");
//                } else {
//                    sb.append(line);
//                    sb.append("\n");
//                }
//            }
//
//            input.close();
//
//            if (found) {
//                PrintWriter out = new PrintWriter(backingStore);
//                out.write(sb.toString());
//                out.close();
//            }
//
//        } catch (NumberFormatException | IOException nfe) {
//            found = false;
//        }
//
//        if (!found) {
//            throw new NotFoundException();
//        }
//    }

    /**
     * Install the key-data pair into the cache as a CacheItem,
     * evicting a previously accessed cache item if necessary.
     * Returns the index in which the CacheItem was stored.
     * If the cache is not full, the data is installed
     * in the smallest index which is empty.
     * This method does not update ranks of cache items, it
     * only manually installs the data into the cache.
     *
     * @param key the key of the data to install in the cache.
     * @param data the data to install in the cache.
     * @return the index in the cache where the CacheItem is installed.
     */
    protected int installData(int key, int data) {
        int insertIndex = -1;
        if (this.size >= this.capacity) {
            insertIndex = evictData(key);
        }

        if (insertIndex < 0) {
            for (int i = 0; i < this.capacity; i++) {
                if (this.data[i] == null) {
                    insertIndex = i;
                    break;
                }
            }
        }

        this.data[insertIndex] = new CacheItem(key, data);
        this.size++;
        return insertIndex;
    }

    /**
     * Find the index in the cache of where to evict data
     * to make room for the incoming key-data pair to be installed.
     * If the cache is not full, returns -1 to indicate no eviction required.
     *
     * @param inKey the incoming key to be installed
     * @return the index in the cache from where to evict old data
     */
    protected int findEvictCandidate(int inKey) {
        int max = -1;
        int maxIndex = -1;

        for (int i = 0; i < this.capacity; i++) {
            if (this.rank[i] > max) {
                max = this.rank[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    /**
     * Evict data from the cache to make room for incoming data
     * whose key is the argument inKey.
     * The data to be evicted is the item with highest rank,
     * i.e. the cache item accessed furthest in the past.
     * Returns the index of the evicted data.
     * If the cache is not full or the incoming key is already in the cache,
     * no data is evicted and -1 is returned.
     *
     * @param inKey the incoming key to be installed
     * @return the index where data was evicted or -1
     */
    public int evictData(int inKey) {
        if (size < capacity) {
            return -1;
        }
        int index = findData(inKey);
        if (index > 0) {
            return -1;
        }

        index = findEvictCandidate(inKey);
        this.data[index] = null;
        this.size--;
        return index;
    }

    /**
     * Get a copy of the current contents of the cache
     * as a list of CacheItems. The returned list has size
     * equal to the capacity of the cache and index in the returned list
     * is equal to the index of the CacheItem stored in the cache.
     * If the cache is not full, indices not holding data
     * are set to null in the returned list.
     *
     * @return a list of CacheItems
     */
    public ArrayList<CacheItem> getContents() {
        ArrayList<CacheItem> contents = new ArrayList<CacheItem>(this.capacity);
        for (int i = 0; i < this.capacity; i++) {
            if (data[i] != null) {
                contents.add(data[i].copy());
            } else {
                contents.add(null);
            }
        }

        return contents;
    }
}
