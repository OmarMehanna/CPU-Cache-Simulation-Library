public class BackingStoreResponse {

    private int data;
    private double timeTaken;

    public BackingStoreResponse(int data, double timeTaken) {
        this.data = data;
        this.timeTaken = timeTaken;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }
}
