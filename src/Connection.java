import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {

    private static DatagramSocket Socket;
    private static InetAddress RecAddress;
    private static Integer RecPort;
    private static String message;
    private static DatagramPacket packet;

    @Override
    public void run() {
        try {
            read();
            message = new String(packet.getData(),0,packet.getLength());
            Message msg = new Message(message);
            System.out.println(msg.getMessage(message));
            Integer lost = Random();
            System.out.println("lost:"+lost);
            if(!(lost < Main.getLossPercentage())){
                if(msg.getType(message).equals("User")){
                    System.out.println(msg.getMessage(message));
                }
                if(msg.getType(message).equals("System")){
                    if(msg.Control(msg,msg.getID(message))){
                        msg.DeleteControl(msg, msg.getID(message));
                    }
                }else{
                    message = msg.setType(message,"System");
                    Sender temp = new Sender(message, Socket);
                    new Thread(temp).start();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Connection(Socket _socket) throws IOException{
        DatagramSocket sd = new DatagramSocket(_socket.getPort(),_socket.getInetAddress());
        if(!Main.getNeighbors().contains(sd)){
            Socket = sd;
            Main.setNeighbors(Socket);
        }else{
            for(int i=0;i<Main.getNeighbors().size();i++){
                if(Main.getNeighbors().get(i).equals(sd)){
                    Socket = Main.getNeighbors().get(i);
                }
            }
        }
        RecAddress = Socket.getInetAddress();
        RecPort = Socket.getPort();
        this.run();
    }

    public void read() throws IOException {
        if(!Socket.isClosed()){
            byte[] buf = new byte[2000000];
            packet = new DatagramPacket(buf,buf.length);
            Socket.receive(packet);
        }
    }

    private int Random(){
        int a = 0;
        int b = 99;
        return (int) (a + Math.random()*b);
    }
}
