import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",5002);
            BingoInterface stub = (BingoInterface) registry.lookup("Bingo");
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            BingoRequest bingoRequest;
            while ((bingoRequest = (BingoRequest) input.readObject()) != null) {

                if (bingoRequest.GetOperationType() == GatewayOperation.Quit) {
                    break;
                }
                else {
                    switch (bingoRequest.GetOperationType()) {
                        case GatewayOperation.Play:
                            int score = stub.Play(bingoRequest.GetClientID(), bingoRequest.GetGuessedNumber());
                            output.writeObject(score);
                            output.flush();
                            break;
                        case GatewayOperation.BestScore:
                            int bestScore = stub.GetBestScore(bingoRequest.GetClientID());
                            output.writeObject(bestScore);
                            output.flush();
                            break;
                        case GatewayOperation.History:
                            List<Integer> scoreHistory = stub.GetClientHistory(bingoRequest.GetClientID());
                            output.writeObject(scoreHistory);
                            output.flush();
                            break;
                    }
                }

            }

            System.out.println("connection closed");
            input.close();
            output.close();
            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Erreur dans ClientHandler: " + e);
            e.printStackTrace();
        }
    }
}