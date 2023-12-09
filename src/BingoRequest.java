import java.io.Serializable;

public class BingoRequest implements Serializable {
    private int idJoueur;
    private int numero;

    public BingoRequest(int idJoueur, int numero) {
        this.idJoueur = idJoueur;
        this.numero = numero;
    }

    // Getters

    public int getIdJoueur() {
        return idJoueur;
    }

    public int getNumero() {
        return numero;
    }
}
