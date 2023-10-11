import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class HangmanServer {

    //list of word to choose from
    private static final String[] WORDS = {"python\n", "hangman\n", "programming\n", "socket\n"};

    //share list on character that already have been guessed by client
    private static final CopyOnWriteArrayList<Character> guessedLetters = new CopyOnWriteArrayList<>();

    //String word
    private static String word;

    public static void main(String[] args) {
        //random and get a word
        word = WORDS[new Random().nextInt(WORDS.length)]; 
        //print out the word in server side
        System.out.println("Word to guess: " + word);

        try {
            //create server socker, pass in port number
            ServerSocket serverSocket = new ServerSocket(8877);
            System.out.println("Server listening on port 8877...");

            while (true) {
                //create socket from serverSocker.accept
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            //create output stream object used to obtain anoutputstream 
            OutputStream outputStream = clientSocket.getOutputStream();
            StringBuilder newGuessedWord = new StringBuilder();
            for (int i = 0; i < word.length()-1; i++) {
                if (guessedLetters.contains(word.charAt(i))) {
                        newGuessedWord.append(word.charAt(i));
                } else {
                    newGuessedWord.append("_");
                }
            }
            newGuessedWord.append('\n');
            String guessedWord = newGuessedWord.toString();
            outputStream.write((guessedWord).getBytes());
            outputStream.flush(); 
            int tryCount = 10;

            while (!guessedWord.equals(word) && guessedWord.contains("_")) {

                byte[] buffer = new byte[1024];
                int bytesRead = clientSocket.getInputStream().read(buffer);
                String guess = new String(buffer, 0, bytesRead).trim().toLowerCase().substring(0, 1);

                synchronized (guessedLetters) {
                    if (!guessedLetters.contains(guess.charAt(0))) {
                        guessedLetters.add(guess.charAt(0));
                    }
                }

                newGuessedWord = new StringBuilder();
                for (int i = 0; i < word.length()-1; i++) {
                    if (guessedLetters.contains(word.charAt(i))) {
                        newGuessedWord.append(word.charAt(i));
                    } else {
                        newGuessedWord.append("_");
                    }
                }
                
                newGuessedWord.append('\n');
                guessedWord = newGuessedWord.toString();
                tryCount = tryCount - 1;
                outputStream.write((guessedWord + "\nYou got " + tryCount + " tries left.").getBytes());
                outputStream.flush();  

                if (tryCount == 0){
                    outputStream.write(("No more try left\n").getBytes());
                    break;
                }
            }

            if (guessedWord.equals(word)) {
                outputStream.write("Congratulations! You guessed the word.\n".getBytes());
            } else {
                outputStream.write(("Sorry, you lost. The word was " + word + ".\n").getBytes());
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
