import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BingoGateway {
    private static final int PORT = 5001;
    private ExecutorService pool;

    public BingoGateway() {
        pool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Gateway started, waiting for clients");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected Client: " + clientSocket.getInetAddress());
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BingoGateway().start();
    }

}
