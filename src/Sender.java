import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;


public class Sender implements Runnable{

    private static Message Message;
    private static String Status;
    private static Socket socket;

    @Override
    public void run() {
        try {
            if(!Receiver.getNeighbors().isEmpty()) {
                if (!Status.equals("resending")) {
                    for (int i=0;i<Receiver.getNeighbors().size();i++) {
                        if (!(Receiver.getNeighbors().get(i).getInetAddress().equals(Message.getSenderIP()))){
                            if(Receiver.getNeighbors().get(i).getPort() != Message.getSenderPort()){
                                SendMsg(Message, Receiver.getNeighbors().get(i));
                                if(!Receiver.Control(Message.getID()))
                                    Receiver.setControl(Message,socket);
                                }
                            }
                        }
                    }else {
                    SendMsg(Message, socket);
                }
            }
        } catch (IOException e) {
            System.out.println("User closed connection");
            e.printStackTrace();

        }
    }

    public Sender(Message msg, Socket _socket){
        Message = msg;
        Status = "resending";
        socket = _socket;
    }

    public Sender(Message msg){
        Message = msg;
        Status = "";
    }

    private void SendMsg(Message msg, Socket socket) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(msg);
        out.flush();
    }
}
