import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;


public class Ping implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                if(!Chat_Tree.getNeighbors().isEmpty()) {
                    Message msg = new Message("Ping", "Ping");
                    Sender send = new Sender(msg,Chat_Tree.getMyIP(),Chat_Tree.getOwnPort());
                    new Thread(send).start();
                    System.out.println("Ping");
                    Thread.sleep(5000);
                    for (Map.Entry<InetAddress, Integer> temp : Chat_Tree.getNeighbors().entrySet()) {
                        if (Chat_Tree.Control(msg.getID()) && LocalTime.now().isAfter(msg.getTime().plusSeconds(30000))) {
                            System.out.println("Delete member");
                            Chat_Tree.DeleteNeighbor(temp.getKey(), temp.getValue());
                        }
                    }
                    if(!Chat_Tree.getSendingControl().isEmpty()) {
                        for (Map.Entry<Message, Socket> tmp : Chat_Tree.getSendingControl().entrySet()) {
                            if (LocalTime.now().isAfter(tmp.getKey().getTime().plusSeconds(3))) {
                                Sender sender = new Sender(tmp.getKey(), tmp.getValue());
                                new Thread(sender).start();
                                System.out.println("Resending");
                            }
                        }
                    }
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
