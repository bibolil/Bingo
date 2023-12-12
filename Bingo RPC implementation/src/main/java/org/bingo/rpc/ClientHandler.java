package org.bingo.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    private List<Integer> GetListFromResponse(Object response) {
        if(response instanceof Object[] responseArray) {
            List<Integer> scoreHistory = new ArrayList<>();
            for (Object obj : responseArray) {
                scoreHistory.add((Integer) obj);
            }
            return  scoreHistory;
        }
        return Collections.emptyList();
    }

    public void run() {
        try {
            ;
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new java.net.URL("http://localhost:5002")); // Replace with your server URL
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            BingoRequest bingoRequest;
            while ((bingoRequest = (BingoRequest) input.readObject()) != null) {

                if (bingoRequest.GetOperationType() == GatewayOperation.Quit) {
                    break;
                }
                else {
                    switch (bingoRequest.GetOperationType()) {
                        case Play:
                            Object[] play_params = new Object[] { bingoRequest.GetClientID(),bingoRequest.GetGuessedNumber()};
                            int score = (int) client.execute("Bingo.Play",play_params);
                            output.writeObject(score);
                            output.flush();
                            break;
                        case BestScore:
                            Object[] bestscore_params = new Object[] { bingoRequest.GetClientID()};
                            int bestScore =  (int) client.execute("Bingo.GetBestScore",bestscore_params);
                            output.writeObject(bestScore);
                            output.flush();
                            break;
                        case History:
                            Object[] history_params= new Object[] { bingoRequest.GetClientID()};
                            Object response = client.execute("Bingo.GetClientHistory", history_params);
                            List<Integer> scoreHistory = GetListFromResponse(response);
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