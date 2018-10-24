import java.io.IOException;
import java.net.*;


public class Sender extends Message implements Runnable {

    private Message message;
    private String Status;
    private DatagramSocket socket;
    private DatagramPacket packet;

    @Override
    public void run() {
        try {
            if(!Main.getNeighbors().isEmpty()) {
                if (!Status.equals("resending")) {
                    for (int i=0;i<Main.getNeighbors().size();i++) {
                        if (!(Main.getNeighbors().get(i).getInetAddress().equals(socket.getInetAddress()))){
                            if(Main.getNeighbors().get(i).getPort() != socket.getPort()){
                                SendMsg(getMessage(message), socket);
                                if(!Control(message,getID(message.getMessage())));
                                    setControl(message,socket);
                            }
                        }
                    }
                }else {
                    SendMsg(getMessage(message), socket);
                }
            }
        } catch (IOException e) {
            System.out.println("User closed connection");
            e.printStackTrace();

        }
    }

    public Sender(String msg,DatagramSocket _socket){
        super(msg,"User");
        Status = "resending";
        socket = _socket;
    }

    public Sender(String msg,String Type){
        super(msg,Type);
        Status = "";
    }
    public Sender(Message msg){
        Status = "";
    }

    private void SendMsg(String msg, DatagramSocket socket) throws IOException {
        if(!socket.isClosed()){
            packet = new DatagramPacket(msg.getBytes(),msg.length(),new InetSocketAddress(socket.getInetAddress(),socket.getPort()));
            socket.send(packet);
        }
    }
}