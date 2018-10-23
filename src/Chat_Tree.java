import javax.swing.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Chat_Tree {

    private static boolean ROOT;
    private static InetAddress MyIP;
    private static Integer LossPercentage;
    private static Integer OwnPort;


    private static Integer ParentPort;
    private static InetAddress ParentIP;
    private static Map<InetAddress,Integer> Neighbors;
    private static Map<Message,Socket> SendingControl;


    public static void main(String args[]){
        try {
            if (args.length != 3 && args.length != 5) {
                JOptionPane.showMessageDialog(null, "Wrong arguments");
                return;
            }
            Neighbors = new HashMap<>();
            SendingControl = new HashMap<>();
            if (args.length == 3) {
                setROOT(true);
                Chat_Tree CT = new Chat_Tree(args[0], args[1], args[2]);
            }
            if (args.length == 5) {
                setROOT(false);
                Chat_Tree CT = new Chat_Tree(args[0], args[1], args[2], args[3], args[4]);
            }
            Listener listen = new Listener();
            Receiver receive = new Receiver();
            Ping ping = new Ping();
            Message msg = new Message("New member", "User");
            Sender send = new Sender(msg);
            new Thread(receive).start();
            new Thread(listen).start();
            new Thread(ping).start();
            new Thread(send).start();
        } catch (SocketException | UnknownHostException | PortException e) {
            e.printStackTrace();
        }
    }

    private Chat_Tree(String nodename, String lossPercentage, String port) throws PortException, UnknownHostException, SocketException{
        MyIP = InetAddress.getByName(nodename);
        LossPercentage = Integer.valueOf(lossPercentage);
        OwnPort = Integer.valueOf(port);
        if(OwnPort < 1 || OwnPort > 65535){
            throw new PortException("Wrong number of the port");
        }
    }

    private Chat_Tree(String nodename, String lossPercentage, String port, String parentPort, String parentIP) throws PortException, UnknownHostException, SocketException {
        MyIP = InetAddress.getByName(nodename);
        LossPercentage = Integer.valueOf(lossPercentage);
        OwnPort = Integer.valueOf(port);
        ParentPort = Integer.valueOf(parentPort);
        ParentIP = InetAddress.getByName(parentIP);
        if(OwnPort < 1 || OwnPort > 65535 || ParentPort < 1 || ParentPort > 65535){
            throw new PortException("Wrong number of the port");
        }
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
