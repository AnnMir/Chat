import java.io.IOException;
import java.net.*;

public class Receiver implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(Main.MyPort);
            while(true){
                System.out.println("Ready to receive messages");
                Socket socket = server.accept();
                System.out.println("Received message");
                new Connection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}