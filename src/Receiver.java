import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(Chat_Tree.getOwnPort());
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
