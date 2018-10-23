import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;


public class Ping implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                for(Map.Entry<InetAddress,Integer> tmp: Receiver.getNeighbors().entrySet())
                System.out.println(tmp.getKey().toString()+tmp.getValue().toString());
                if(!Receiver.getNeighbors().isEmpty()) {
                    Message msg = new Message("Ping", "Ping");
                    Sender send = new Sender(msg,Receiver.getMyIP(),Receiver.getOwnPort());
                    System.out.println(Receiver.getMyIP());
                    new Thread(send).start();
                    System.out.println("Ping");
                    Thread.sleep(5000);
                    for (Map.Entry<InetAddress, Integer> temp : Receiver.getNeighbors().entrySet()) {
                        if (Receiver.Control(msg.getID()) && LocalTime.now().isAfter(msg.getTime().plusSeconds(30000))) {
                            System.out.println("Delete member");
                            Receiver.DeleteNeighbor(temp.getKey(), temp.getValue());
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
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
