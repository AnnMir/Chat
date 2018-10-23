import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Listener implements Runnable {
    @Override
    public void run() {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String msg = null;
        while (true) {
            try {
                msg = userInput.readLine();
                Message message = new Message(msg, "User");
                Sender send = new Sender(message,Chat_Tree.getMyIP(),Chat_Tree.getOwnPort());
                new Thread(send).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
