import javax.swing.*;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    private static BlockingDeque<DatagramSocket> Neighbors;
    private static InetAddress MyIP;
    private static Integer LossPercentage;
    public static Integer MyPort;


    private static Integer ParentPort;
    private static InetAddress ParentIP;
    public static Map<Message,DatagramSocket> SendingControl;

    public static void main(String args[]){
        try {
            if (args.length != 3 && args.length != 5) {
                JOptionPane.showMessageDialog(null, "Wrong arguments");
                return;
            }
            MyIP = InetAddress.getByName(args[0]);
            LossPercentage = Integer.valueOf(args[1]);
            MyPort = Integer.valueOf(args[2]);
            Neighbors = new LinkedBlockingDeque<>();
            SendingControl = new HashMap<>();
            if(MyPort < 1 || MyPort > 65535){
                throw new PortException("Wrong number of port");
            }
            if (args.length == 5) {
                ParentPort = Integer.valueOf(args[3]);
                ParentIP = InetAddress.getByName(args[4]);
                if(ParentPort < 1 || ParentPort > 65535)
                    throw new PortException("Wrong number of parent port");
                DatagramSocket socket = new DatagramSocket(ParentPort,ParentIP);
                setNeighbors(socket);
            }
            Listener listen = new Listener();
            Ping ping = new Ping();
            Receiver receive = new Receiver();
            if(!getNeighbors().isEmpty()){
                Sender send = new Sender("Hello","User");
                new Thread(send).start();
            }
            new Thread(listen).start();
            new Thread(ping).start();
            new Thread(receive).start();
        } catch (PortException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Message, DatagramSocket> getSendingControl() {
        return SendingControl;
    }

    public static void setNeighbors(DatagramSocket socket) {
        if(!Neighbors.contains(socket))
            Neighbors.add(socket);
    }
    public static BlockingDeque<DatagramSocket> getNeighbors() {
        return Neighbors;
    }

    public static void DeleteNeighbor(DatagramSocket socket){
        Neighbors.remove(socket);
    }

    public static Integer getLossPercentage() {
        return LossPercentage;
    }

    public static InetAddress getMyIP() {
        return MyIP;
    }
}

class PortException extends Exception{
    PortException(String message){
        super(message);
    }
}