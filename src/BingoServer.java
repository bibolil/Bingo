import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BingoServer {
    public static void main(String[] args) {
        try {
            BingoInterface stub = new Bingo();
            Registry registry = LocateRegistry.createRegistry(5002);
            registry.bind("Bingo", stub);
            System.out.println("Server Started");
        } catch (Exception e) {
            System.err.println("Error: " + e);
            e.printStackTrace();
        }
    }
}

