import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Receiver implements Runnable{

    private static Map<InetAddress,Integer> Neighbors;
    private static boolean ROOT;
    public static InetAddress MyIP;
    private static Integer LossPercentage;
    public static Integer OwnPort;


    private static Integer ParentPort;
    private static InetAddress ParentIP;
    private static Map<Message,Socket> SendingControl;

    @Override
    public void run() {
        try {
            Neighbors = new HashMap<>();
            ServerSocket server = new ServerSocket(OwnPort);
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
    public Receiver(String nodename, String lossPercentage, String port) throws PortException, UnknownHostException, SocketException {
        MyIP = InetAddress.getByName(nodename);
        LossPercentage = Integer.valueOf(lossPercentage);
        OwnPort = Integer.valueOf(port);
        if(OwnPort < 1 || OwnPort > 65535){
            throw new PortException("Wrong number of the port");
        }
        Neighbors = new HashMap<>();
    }

    public Receiver(String nodename, String lossPercentage, String port, String parentPort, String parentIP) throws PortException, UnknownHostException, SocketException {
        MyIP = InetAddress.getByName(nodename);
        LossPercentage = Integer.valueOf(lossPercentage);
        OwnPort = Integer.valueOf(port);
        ParentPort = Integer.valueOf(parentPort);
        ParentIP = InetAddress.getByName(parentIP);
        if(OwnPort < 1 || OwnPort > 65535 || ParentPort < 1 || ParentPort > 65535){
            throw new PortException("Wrong number of the port");
        }
        Neighbors = new HashMap<>();
        setNeighbors(ParentIP, ParentPort);
    }

    private static void setROOT(boolean _ROOT) {
        ROOT = _ROOT;
    }

    public static boolean getROOT(){
        return ROOT;
    }

    private static void setNeighbors(InetAddress IP, Integer port) {
        Neighbors.put(IP, port);
    }

    public static Map<InetAddress, Integer> getNeighbors() {
        return Neighbors;
    }

    public static void DeleteNeighbor(InetAddress IP, Integer port){
        Neighbors.remove(IP, port);
    }

    public static Integer getLossPercentage() {
        return LossPercentage;
    }

    public static Integer getOwnPort() {
        return OwnPort;
    }

    public static InetAddress getMyIP() {
        return MyIP;
    }

    public static void setControl(Message msg, Socket socket){
        SendingControl.put(msg,socket);
    }

    public static Map<Message, Socket> getSendingControl() {
        return SendingControl;
    }

    public static boolean Control(String id){
        for(Map.Entry<Message,Socket> tmp: getSendingControl().entrySet()){
            if(tmp.getKey().getID().equals(id))
                return true;
        }
        return false;
    }

    public static void DeleteControl(String id){
        for(Map.Entry<Message,Socket> tmp: getSendingControl().entrySet()){
            if(tmp.getKey().getID().equals(id)){
                SendingControl.remove(tmp.getKey(),tmp.getValue());
            }
        }
    }
}


class PortException extends Exception{
    PortException(String message){
        super(message);
    }
}
