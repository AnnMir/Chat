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
                System.out.println("Enter your message");
                msg = userInput.readLine();
                Sender send = new Sender(msg);
                new Thread(send).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
