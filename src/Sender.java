import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Map;


public class Sender implements Runnable{

    private static InetAddress IP;
    private static Integer Port;
    private static Message Message;
    private static String Status;
    private static Socket socket;

    @Override
    public void run() {
        try {
            if(!Chat_Tree.getNeighbors().isEmpty()) {
                if (!Status.equals("resending")) {
                    for (Map.Entry<InetAddress, Integer> tmp : Chat_Tree.getNeighbors().entrySet()) {
                        if (!tmp.getKey().equals(IP) && !tmp.getValue().equals(Port)) {
                            SendMsg(Message, tmp.getKey(), tmp.getValue());
                            if(!Chat_Tree.Control(Message.getID()))
                                Chat_Tree.setControl(Message,socket);
                        }
                    }
                }else {
                    SendMsg(Message, IP, Port);
                }
            }
        } catch (IOException e) {
            System.out.println("User closed connection");
            e.printStackTrace();

        }
    }

    /*Sender(Message msg){
        Message = msg;
        IP = null;
        Port = null;
        Status = "broadcasting";
    }*/

    public Sender(Message msg, Socket _socket){
        Message = msg;
        IP = _socket.getInetAddress();
        Port = _socket.getPort();
        Status = "resending";
    }

    Sender(Message msg, InetAddress Ip, Integer port){
        Message = msg;
        IP = Ip;
        Port = port;
        Status = "transfer";
    }

    private void SendMsg(Message msg, InetAddress IP, Integer port) throws IOException {
        socket = new Socket(IP, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(msg);
        out.flush();
    }
}
