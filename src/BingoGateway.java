import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BingoGateway {
    private static final int PORT = 5001; // Port d'écoute pour les clients
    private ExecutorService pool; // Pool de threads pour gérer les clients

    public BingoGateway() {
        pool = Executors.newFixedThreadPool(10); // Limite de clients simultanés
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Gateway démarrée, en attente de clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté: " + clientSocket.getInetAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (Exception e) {
            System.err.println("Erreur Gateway: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BingoGateway().start();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            ) {
                Registry registry = LocateRegistry.getRegistry("localhost",5002);
                BingoInterface stub = (BingoInterface) registry.lookup("Bingo");

                // Logique de communication avec le client et le serveur RMI ici...
                while (!clientSocket.isClosed()) {
                    // Attendre et recevoir une requête du client
                    Object requestObject = input.readObject();

                    if (requestObject instanceof BingoRequest) {
                        BingoRequest request = (BingoRequest) requestObject;
                        int idJoueur = request.getIdJoueur();
                        int numero = request.getNumero();

                        // Appel de la méthode jouer sur le serveur RMI
                        int resultat = stub.jouer(idJoueur, numero);

                        // Création et envoi de la réponse au client
                        BingoResponse response = new BingoResponse(resultat);
                        output.writeObject(response);
                        output.flush();
                    }}


            } catch (Exception e) {
                System.err.println("Erreur dans ClientHandler: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
