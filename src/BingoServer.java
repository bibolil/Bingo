import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BingoServer {
    public static void main(String[] args) {
        try {
            BingoInterface stub = new BingoImpl();
            Registry registry = LocateRegistry.createRegistry(5002);
            registry.bind("Bingo", stub);
            System.out.println("Serveur Bingo démarré.");
        } catch (Exception e) {
            System.err.println("Erreur serveur: " + e);
            e.printStackTrace();
        }
    }
}

