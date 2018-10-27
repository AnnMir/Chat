import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class Sender implements Runnable{

    private DatagramSocket myDatagramSocket;
    private Set<String> neighboursList;
    private String myName;
    private LinkedBlockingQueue<Message> toSendQueue;
    private ConcurrentHashMap<String, ConfirmerData> myUnconfirmed;

    //конструктор для рута
    public Sender(String ip, DatagramSocket datagramSocket,ConcurrentHashMap<String , ConfirmerData> unconfirmed
            ,Set<String> neighbours){
        myName =ip;
        myDatagramSocket=datagramSocket;
        toSendQueue=new LinkedBlockingQueue<>();
        myUnconfirmed =unconfirmed;
        neighboursList=neighbours;
    }

    //констурктор для потомков
    public Sender(String ip,DatagramSocket datagramSocket,InetAddress parentIp,int parentPort
            ,ConcurrentHashMap<String, ConfirmerData> unconfirmed,Set<String> neighbours){
        myName = ip;
        myDatagramSocket=datagramSocket;
        neighboursList=neighbours;
        neighboursList.add(parentIp.getHostAddress()+":"+parentPort);
        toSendQueue=new LinkedBlockingQueue<>();
        myUnconfirmed =unconfirmed;
        sendHello();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message ms = toSendQueue.take();
                sendMessage(ms);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //кладем сообщение в очередь сообщений
    void putMessageToQueue(String uuid, String head, String msg, String senderName, String previousSender){
        Message message;
        if(uuid==null || senderName==null ) {
            message = new Message(UUID.randomUUID().toString(), Constants.MESSAGE_HEADER, msg, myName, null);
        }
        else {
            message = new Message(uuid, head, msg, senderName, previousSender);
        }
        try {
            toSendQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //рассылка сообщения всем соседям кроме предыдущего отправителя
    private void sendMessage(Message message){
        try {
            String neighbourIp;
            String neighbourPort;
            Set<String> myReceivers=ConcurrentHashMap.newKeySet();
            myReceivers.addAll(neighboursList);

            myUnconfirmed.putIfAbsent(message.getUuid()
                    , new ConfirmerData(message.toString(),myReceivers,message.getNameFrom()));

            for(String currentNeighbour:neighboursList) {
                if(currentNeighbour.equals(message.getNameFrom())) {
                    continue;
                }
                String[] receiverStringsAddress = currentNeighbour.split(":");
                InetSocketAddress receiverAddress = new InetSocketAddress(receiverStringsAddress[0]
                        , Integer.parseInt(receiverStringsAddress[1]));
                byte[] buffer = message.toString().getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length
                        , receiverAddress);
                myDatagramSocket.send(datagramPacket);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //отправка уведомительного сообщения родителю
    private void sendHello(){
        putMessageToQueue(UUID.randomUUID().toString(), Constants.CHILD_HEADER,"", myName,myName);
    }

    String getMyName(){
        return myName;
    }
}