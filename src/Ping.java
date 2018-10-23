import java.net.InetAddress;
import java.util.Map;

public class Ping implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                if(!Chat_Tree.getNeighbors().isEmpty()) {
                    Message msg = new Message("Ping", "System");
                    Sender send = new Sender(msg);
                    new Thread(send).start();
                    System.out.println("Ping");
                    Thread.sleep(20000);
                    for (Map.Entry<InetAddress, Integer> temp : Chat_Tree.getNeighbors().entrySet()) {
                        if (Chat_Tree.Control(msg.getID())) {
                            System.out.println("Delete member");
                            Chat_Tree.DeleteNeighbor(temp.getKey(), temp.getValue());
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
