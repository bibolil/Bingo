import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.HashMap;

public class BingoImpl extends UnicastRemoteObject implements BingoInterface {
    private HashSet<Integer> boules;
    private int meilleurScore;
    private HashMap<Integer, Integer> scoresDesJoueurs; // Nouveau

    public BingoImpl() throws RemoteException {
        super();
        this.meilleurScore = 0;
        this.scoresDesJoueurs = new HashMap<>();
        initialiserBoules();
    }

    private void initialiserBoules() {
        boules = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            boules.add(i);
        }
    }

    public int jouer(int idJoueur, int numero) throws RemoteException {
        int score = boules.contains(numero) ? 1 : 0;
        boules.remove(numero);
        calculerScore(idJoueur, score);
        return score;
    }

    private synchronized void calculerScore(int idJoueur, int score) {
        int scoreActuel = scoresDesJoueurs.getOrDefault(idJoueur, 0);
        scoreActuel += score;
        scoresDesJoueurs.put(idJoueur, scoreActuel);
        mettreAJourMeilleurScore(scoreActuel);
    }

    public int obtenirMeilleurScore() throws RemoteException {
        return meilleurScore;
    }

    public void mettreAJourMeilleurScore(int score) {
        if (score > meilleurScore) {
            meilleurScore = score;
        }
    }
}
