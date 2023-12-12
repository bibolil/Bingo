package org.bingo.rpc;
import org.apache.xmlrpc.XmlRpcException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Bingo implements BingoInterface{
    private final ConcurrentHashMap<Integer, List<Game>> _clientGames;

    public Bingo() throws RemoteException {
        super();
        this._clientGames = new ConcurrentHashMap<>();
    }

    public int Play(int clientID, int guessedNumber) throws XmlRpcException {
        if(!_clientGames.containsKey(clientID))
            _clientGames.put(clientID, new ArrayList<>());

        List<Game> clientGames = _clientGames.get(clientID);
        synchronized (clientGames) {
            if (clientGames.isEmpty()) {
                System.out.println("first game for " + clientID);
                clientGames.add(new Game());
            }
            Game clientLastGame = clientGames.getLast();
            if (clientLastGame.isEnded()) {
                System.out.println("new game for " + clientID);
                clientLastGame = new Game();
                clientGames.add(clientLastGame);
            }
            return clientLastGame.Guess(guessedNumber);
        }
    }

    public int GetBestScore(int clientID) throws  XmlRpcException {
        if(!_clientGames.containsKey(clientID))
            return 0;

        List<Game> clientGames = _clientGames.get(clientID);
        int maxGameScore = Integer.MIN_VALUE;
        for (Game game : clientGames)
            if (game.GetGameScore() > maxGameScore)
                maxGameScore = game.GetGameScore();

        return maxGameScore;
    }

    public List<Integer> GetClientHistory(int clientID) throws XmlRpcException {
        List<Integer> clientHistory = new ArrayList<>();
        if(!_clientGames.containsKey(clientID))
            return clientHistory;

        List<Game> clientGames = _clientGames.get(clientID);
        for (Game game : clientGames)
            clientHistory.add(game.GetGameScore());
        return clientHistory;
    }
}
