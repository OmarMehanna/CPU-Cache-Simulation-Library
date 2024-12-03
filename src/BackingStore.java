import java.io.*;

public class BackingStore {

    private String fileName;


    public BackingStore(String fileName) {
        this.fileName = fileName;
    }



    /**
     * Write data to the backing store.
     * Given integers of a key and newData, try to update
     * the backing store by finding the key and changing
     * the associated data to newData.
     * The order of keys in the backing store does not change.
     * If the key is not found in the backing store, throws
     * a NotFoundException.
     * @param key the key of the data item to update
     * @param newData the new data to write
     * @throws NotFoundException if the specified key is not found
     */
    public void pushData(int key, int newData) throws NotFoundException {
        boolean found = false;
        String cmpKey = Integer.toString(key);
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader input = new BufferedReader(new FileReader(this.fileName));
            String line;
            while ((line = input.readLine()) != null) {
                String[] vals = line.split(" ");
                if (vals.length == 2 && vals[0].equals(cmpKey)) {
                    found = true;
                    sb.append(vals[0]);
                    sb.append(" ");
                    sb.append(newData);
                    sb.append("\n");
                } else {
                    sb.append(line);
                    sb.append("\n");
                }
            }

            input.close();

            if (found) {
                PrintWriter out = new PrintWriter(new FileWriter(this.fileName));
                out.write(sb.toString());
                out.close();
            }

        } catch (NumberFormatException | IOException nfe) {
            found = false;
        }

        if (!found) {
            throw new NotFoundException();
        }
    }


    /**
     * Fetch data from the backing store given the associated key.
     * This method only fetches the data and returns it;
     * it does not interact with the cache at all.
     * If the key is not found in the backing store, throw
     * a NotFoundException.
     *
     * @param key the key of the requested data item
     * @return the data from the backing store with requested key if found
     * @throws NotFoundException if the requested key is not found
     */
    public BackingStoreResponse fetchData(int key) throws NotFoundException {
        int data = 0;

        boolean found = false;
        String cmpKey = Integer.toString(key);
        double timetaken = 0.0;
        try {
            BufferedReader input = new BufferedReader(new FileReader(this.fileName));
            String line;
            while (!found && (line = input.readLine()) != null) {
                timetaken += 1.0;
                String[] vals = line.split(" ");
                if (vals.length == 2 && vals[0].equals(cmpKey)) {
                    data = Integer.parseInt(vals[1]);
                    found = true;
                }
            }

            input.close();

        } catch (NumberFormatException | IOException nfe) {
            found = false;
        }

        if (!found) {
            throw new NotFoundException();
        }
        return new BackingStoreResponse(data, timetaken);
    }
}
