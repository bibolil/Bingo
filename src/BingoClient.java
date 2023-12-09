import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class BingoClient {
    private static final String GATEWAY_HOST = "localhost";
    private static final int GATEWAY_PORT = 5001;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(GATEWAY_HOST, GATEWAY_PORT);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Scanner scanner = new Scanner(System.in);
        ) {
            int score = 0;
            int idJoueur = (int) (Math.random() * 1000); // ID unique pour chaque joueur

            for (int i = 0; i < 10; i++) {
                System.out.println("Tentative " + (i + 1) + "/10");
                System.out.print("Entrez un numéro entre 0 et 9: ");
                int supposition = scanner.nextInt();

                // Envoyer la supposition à la Gateway
                output.writeObject(new BingoRequest(idJoueur, supposition));
                output.flush();

                // Recevoir la réponse de la Gateway
                BingoResponse response = (BingoResponse) input.readObject();
                if (response.getResult() == 1) {
                    System.out.println("Correct !");
                    score++;
                } else {
                    System.out.println("Incorrect. Essayez encore.");
                }
            }

            System.out.println("Votre score final est : " + score + "/10");
            // Envoyer une requête pour obtenir le meilleur score
            // ...

        } catch (Exception e) {
            System.err.println("Erreur client: " + e.toString());
            e.printStackTrace();
        }
    }
}
