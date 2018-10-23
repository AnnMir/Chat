import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Map;


public class Sender implements Runnable{

    private static InetAddress IP;
    private static Integer Port;
    private static Message Message;
    private static String Status;

    @Override
    public void run() {
        try {
            if(!Chat_Tree.getNeighbors().isEmpty()) {
                if (!Status.equals("resending")) {
                    for (Map.Entry<InetAddress, Integer> tmp : Chat_Tree.getNeighbors().entrySet()) {
                        if (!tmp.getKey().equals(IP) && !tmp.getValue().equals(Port)) {
                            Socket temp = SendMsg(Message, tmp.getKey(), tmp.getValue());
                            if(!Chat_Tree.Control(Message.getID()))
                                Chat_Tree.setControl(Message,temp);
                        }
                    }
                }else {
                    SendMsg(Message, IP, Port);
                }
            }
        } catch (IOException e) {
            System.out.println("User closed connection");
            //e.printStackTrace();

        }
    }

    Sender(Message msg){
        Message = msg;
        IP = null;
        Port = null;
        Status = "broadcasting";
    }

    public Sender(Message msg, Socket Socket){
        Message = msg;
        IP = Socket.getInetAddress();
        Port = Socket.getPort();
        Status = "resending";
    }

    Sender(Message msg, InetAddress Ip, Integer port){
        Message = msg;
        IP = Ip;
        Port = port;
        Status = "transfer";
    }

    private Socket SendMsg(Message msg, InetAddress IP, Integer port) throws IOException {
        Socket temp = new Socket(IP, port);
        ObjectOutputStream out = new ObjectOutputStream(temp.getOutputStream());
        out.writeObject(msg);
        out.flush();
        return temp;
    }
}
