import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

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
                System.out.println("message: "+message.getMessage()+"Type: "+message.getType());
                Integer lost = Random();
                System.out.println("lost:"+lost);
                if(!(lost < Chat_Tree.getLossPercentage())){
                    if(message.getType().equals("User")){
                        System.out.println(message.getMessage());
                    }
                    if(message.getType().equals("System")){
                        if(Chat_Tree.Control(message.getID())){
                            Chat_Tree.DeleteControl(message.getID());
                        }
                    }else{
                        message.setType("System");
                        Sender temp = new Sender(message, RecAddress, RecPort);
                        new Thread(temp).start();
                        out.writeObject(message);
                        out.flush();
                    }
                }
                Chat_Tree.getNeighbors().putIfAbsent(RecAddress,RecPort);
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
