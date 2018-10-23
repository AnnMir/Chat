import javax.swing.*;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    public static void main(String args[]){
        try {
            if (args.length != 3 && args.length != 5) {
                JOptionPane.showMessageDialog(null, "Wrong arguments");
                return;
            }
            if (args.length == 3) {
                Receiver receive = new Receiver(args[0], args[1], args[2]);
                new Thread(receive).start();
            }
            if (args.length == 5) {
                Receiver receive = new Receiver(args[0], args[1], args[2], args[3], args[4]);
                new Thread(receive).start();
            }
            Listener listen = new Listener();
            Ping ping = new Ping();
            Message msg = new Message("New member", "User");
            Sender send = new Sender(msg,Receiver.MyIP,Receiver.OwnPort);
            new Thread(listen).start();
            new Thread(send).start();
            new Thread(ping).start();
        } catch (SocketException | UnknownHostException | PortException e) {
            e.printStackTrace();
        }
    }
}
