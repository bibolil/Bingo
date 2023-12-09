import java.io.Serializable;

public class BingoResponse implements Serializable {
    private int result;

    public BingoResponse(int result) {
        this.result = result;
    }

    // Getters

    public int getResult() {
        return result;
    }
}
