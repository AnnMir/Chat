import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;


public class Ping implements Runnable{

    @Override
    public void run() {
        /*while(true){
            try {
                if(!Receiver.getNeighbors().isEmpty()) {
                    GUID guid = new GUID();
                    String msg = guid.getID()+" "+"Ping"+" "+"Ping";
                    Message msg
                    Sender send = new Sender(msg);
                    System.out.println(Receiver.getMyIP());
                    new Thread(send).start();
                    System.out.println("Ping");
                    Thread.sleep(5000);
                    if (Main.Control(.getID(msg)) && LocalTime.now().isAfter(msg.getTime().plusSeconds(30000))) {
                        System.out.println("Delete member");
                        Receiver.DeleteNeighbor(Receiver.getSendingControl().get(msg));
                    }
                }
                if(!Receiver.getSendingControl().isEmpty()) {
                        for (Map.Entry<Message, Socket> tmp : Receiver.getSendingControl().entrySet()) {
                            if (LocalTime.now().isAfter(tmp.getKey().getTime().plusSeconds(3))) {
                                Sender sender = new Sender(tmp.getKey(), tmp.getValue());
                                new Thread(sender).start();
                                System.out.println("Resending");
                            }
                        }
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
