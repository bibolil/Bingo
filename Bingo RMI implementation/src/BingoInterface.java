import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BingoInterface extends Remote {
    int Play(int clientID, int guessedNumber) throws RemoteException;
    int GetBestScore(int clientID) throws RemoteException;
    List<Integer> GetClientHistory(int clientID) throws  RemoteException;
}
