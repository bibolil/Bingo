import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

public class BingoClient {

    private static final String GATEWAY_HOST = "localhost";
    private static final int GATEWAY_PORT = 5001;
    private  static int ClientID = 0;

    private static void PlayBingo(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
        int cachedScore = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        for(int round = 1; round <= 10; round++) {
            System.out.println();
            System.out.println("############### Round N°" + round + " out of 10 ###############");
            System.out.print("Guess a number from 0 to 9: ");
            int guessedNumber = scanner.nextInt();
            outputStream.writeObject(new BingoRequest(ClientID, GatewayOperation.Play, guessedNumber));
            outputStream.flush();
            int score = (int) inputStream.readObject();
            if(score > cachedScore)
                System.out.println("Good Job, Your Score Become: " + score);
            else
                System.out.println("Bad Luck, Your Score Is: " + cachedScore);
            cachedScore = score;
        }
        System.out.println("############### Game Over | Score:"+ cachedScore + " / 10 ###############");
    }
    private static void ShowBestScore(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
        System.out.println("############### Show Best Score ###############");
        outputStream.writeObject(new BingoRequest(ClientID, GatewayOperation.BestScore, -1));
        outputStream.flush();
        int bestScore = (int) inputStream.readObject();
        System.out.println("Your Best Score Is " + bestScore);
        System.out.println("############### End ###############");
    }
    private static void ShowHistory(ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
        System.out.println("############### Show History ###############");
        outputStream.writeObject(new BingoRequest(ClientID, GatewayOperation.History, -1));
        outputStream.flush();
        List<Integer> scores = (List<Integer>) inputStream.readObject();

        for (int gameNumber = 0; gameNumber < scores.size(); gameNumber++)
            System.out.println("Game N°" + gameNumber + ": " + scores.get(gameNumber));
        System.out.println("############### End ###############");
    }
    private static void Quit( ObjectOutputStream outputStream) throws Exception {
        outputStream.writeObject(new BingoRequest(ClientID, GatewayOperation.Quit, -1));
        outputStream.flush();

    }

    private static int ShowMenu() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("############### Menu ###############");
        System.out.println("1- Play Bingo");
        System.out.println("2- Show Best Score");
        System.out.println("3- Show History");
        System.out.println("4- Quit");
        System.out.println("###############      ###############");
        System.out.println();
        System.out.print("(?) Please select an option from 1 to 4: ");
        Scanner scanner = new Scanner(System.in);
        int selectedOption = scanner.nextInt();
        while(selectedOption < 1 || selectedOption > 4) {
            System.out.println("[X] Please select a valid option from 1 to 4: ");
            selectedOption = scanner.nextInt();
        }
        return selectedOption;
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(GATEWAY_HOST, GATEWAY_PORT);
            Scanner scanner = new Scanner(System.in);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("############### Welcome to Bingo ###############");
            System.out.print("Please Entre the Client ID: ");
            ClientID = scanner.nextInt();
            boolean playing= true;
            try {
                while (playing) {
                    int option = ShowMenu();
                    switch (option) {
                        case 1: PlayBingo(inputStream, outputStream); break;
                        case 2: ShowBestScore(inputStream, outputStream); break;
                        case 3: ShowHistory(inputStream, outputStream); break;
                        case 4: Quit(outputStream); playing=false; break;
                    }
                    System.out.println("############### End Of Operation ###############");
                }
            } catch (Exception ex) {
                System.out.println("Error while executing one of the options");
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
