import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Map;

public class Connection implements Runnable {

    private static Socket Socket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static InetAddress RecAddress;
    private static Integer RecPort;
    private static Message message;

    @Override
    public void run() {
        try {
                message = (Message)in.readObject();
                Integer lost = Random();
                if(!(lost < Chat_Tree.getLossPercentage())){
                    if(message.getType().equals("User")){
                        System.out.println(message.getMessage());
                        Sender temp = new Sender(message, RecAddress, RecPort);
                        new Thread(temp).start();
                        message.setType("System");
                        out.writeObject(message);
                        out.flush();
                    }
                    if(message.getType().equals("System")){
                        if(Chat_Tree.Control(message.getID())){
                            Chat_Tree.DeleteControl(message.getID());
                        }
                    }
                }
                Chat_Tree.getNeighbors().putIfAbsent(RecAddress,RecPort);
                if(!Chat_Tree.getSendingControl().isEmpty()){
                    for(Map.Entry<Message,Socket> tmp: Chat_Tree.getSendingControl().entrySet()){
                        if(LocalTime.now().isAfter(tmp.getKey().getTime().plusSeconds(3))){
                            Sender send = new Sender(tmp.getKey(),tmp.getValue());
                            new Thread(send).start();
                            System.out.println("Resending");
                        }
                    }
                }
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Connection(Socket _socket) throws IOException{
            Socket = _socket;
            in = new ObjectInputStream(Socket.getInputStream());
            out = new ObjectOutputStream(Socket.getOutputStream());
            RecAddress = Socket.getInetAddress();
            RecPort = Socket.getPort();
            this.run();


    }

    private int Random(){
        int a = 0;
        int b = 99;
        return (int) (a + Math.random()*b);
    }
}
