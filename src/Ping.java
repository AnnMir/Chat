import java.net.DatagramSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;


public class Ping implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                if(!Main.getNeighbors().isEmpty()) {
                    Message msg = new Message("Ping","Ping");
                    Sender send = new Sender(msg);
                    System.out.println(Main.getMyIP());
                    new Thread(send).start();
                    System.out.println("Ping");
                    Thread.sleep(5000);
                    if (msg.Control(msg, msg.getID(msg.getMessage())) && LocalTime.now().isAfter(msg.getTime(msg.getMessage()).plusSeconds(30000))) {
                        System.out.println("Delete member");
                        Main.DeleteNeighbor(Main.getSendingControl().get(msg));
                    }
                }
                if(!Main.getSendingControl().isEmpty()) {
                        for (Map.Entry<Message, DatagramSocket> tmp : Main.getSendingControl().entrySet()) {
                            if (LocalTime.now().isAfter(tmp.getKey().getTime(tmp.getKey().getMessage()).plusSeconds(3))) {
                                Sender sender = new Sender(tmp.getKey().getMessage(), tmp.getValue());
                                new Thread(sender).start();
                                System.out.println("Resending");
                            }
                        }
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
