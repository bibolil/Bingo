import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BingoInterface extends Remote {
    int jouer(int idJoueur, int numero) throws RemoteException;
    int obtenirMeilleurScore() throws RemoteException;
}
