import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HangmanClient2 {

    public static void main(String[] args) {
        try {
            //declare socket with the same port number as Server
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);
            Socket socket = new Socket(hostname, port);
     
            InputStream inputStream = socket.getInputStream();
            
            //read word from server
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String word = new String(buffer, 0, bytesRead).trim();
            System.out.println("Word to guess: " + word);

            //input stream capable of reading bytes string, but we don't want to deal with byte
            //InputStream reader get bytes string and output the character string
            //but like character is not enough -> want string -> Buffer reader capable of reading entire string
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            
            String guess;

            while (true) {
                System.out.print("Enter your guess: ");
                guess = inputReader.readLine().trim().toLowerCase();

                //output stream -> sending data
                socket.getOutputStream().write(guess.getBytes());
                socket.getOutputStream().flush();

                //wait to read input from server, the word like a____.
                bytesRead = inputStream.read(buffer);
                String guessedWord = new String(buffer, 0, bytesRead).trim();
                System.out.println(guessedWord);
                if(bytesRead == -1){
                    System.out.println("Server closed the connection");
                    break;
                }
            
                if (guessedWord.contains("Congratulations") || guessedWord.contains("Sorry")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
