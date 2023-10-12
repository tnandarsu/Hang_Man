import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.ArrayList;

public class HangmanServer3 {

    //list of word to choose from
    private static final String[] WORDS = {"python\n", "hangman\n", "programming\n", "socket\n", "monitor\n", "java\n"};


    public static void main(String[] args) {
        //random and get a word assign to word var

        try {
            //create server socker, pass in port number
            int port =Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port 8877...");

            while (true) {
                //create socket from serverSocker.accept
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //thread
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            String word = WORDS[new Random().nextInt(WORDS.length)]; 
            //create output stream object used to obtain anoutputstream 
            ArrayList<Character> guessedLetters = new ArrayList<>();
            OutputStream outputStream = clientSocket.getOutputStream();


            StringBuilder newGuessedWord = new StringBuilder();
            for (int i = 0; i < word.length()-1; i++) {
                newGuessedWord.append("_");
            }
            newGuessedWord.append('\n');
            String guessedWord = newGuessedWord.toString();

            //sending blank (first time sending)
            outputStream.write((guessedWord).getBytes());
            outputStream.flush(); 

            //break word into array of char
            char[] charArray = word.toCharArray();
            //set the count var
            int tryCount = 10;

            //while the client not correctly guess yet
            while (!guessedWord.equals(word) && guessedWord.contains("_")) {
                
                //read char from client
                byte[] buffer = new byte[1024];
                int bytesRead = clientSocket.getInputStream().read(buffer);
                String guess = new String(buffer, 0, bytesRead).trim().toLowerCase().substring(0, 1);
                char guess_char = guess.charAt(0);
                
                //if guess is in wordArray
                //decrement tryCOunt by 1, if guess incorrectly
                boolean found = false;
                for(char c: charArray) {
                    if (c == guess_char){
                        found = true;
                        break;
                    }
                }
                if(found == false){
                    tryCount = tryCount - 1;
                }
                //add guess to guessLetters array
                guessedLetters.add(guess.charAt(0));
                newGuessedWord = new StringBuilder();

                //generate guess word to send back
                for (int i = 0; i < word.length()-1; i++) {
                    if (guessedLetters.contains(word.charAt(i))) {
                        newGuessedWord.append(word.charAt(i));
                    } else {
                        newGuessedWord.append("_");
                    }
                }
                newGuessedWord.append('\n');
                guessedWord = newGuessedWord.toString();

                //send guessword to client
                outputStream.write((guessedWord + "\nYou got " + tryCount + " tries left.").getBytes());
                outputStream.flush();  

                //if there no try left, exit
                if (tryCount == 1){
                    outputStream.write(("No more try left. Sorry, you lost, The word was " + word + "\n").getBytes());
                    
                    break;
                }
            }

            //congrates, sorry
            if (guessedWord.equals(word)) {
                outputStream.write("Congratulations! You guessed the word.\n".getBytes());
            } 

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
