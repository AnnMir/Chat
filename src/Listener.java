import java.util.Scanner;

public class Listener implements Runnable {
    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String msg = in.nextLine();
            Message message = new Message(msg, "User");
            Sender send = new Sender(message);
            new Thread(send).start();
        }
    }
}
