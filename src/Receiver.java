import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import static java.nio.charset.StandardCharsets.UTF_8;


public class Receiver implements Runnable {
    private DatagramSocket myDatagramSocket;
    private int myPercentLost;
    private String myName;
    private Set<String> myNeighbours;
    private ConcurrentHashMap<String, Date> myRecentReceived;
    private ConcurrentHashMap<String, ConfirmerData> myUnconfirmed;
    private Sender mySender;
    private Listener myListener;

    public Receiver(String name,DatagramSocket datagramSocket,int percentLost,Set<String> neighbours
            , ConcurrentHashMap<String, Date> recentReceived
            , ConcurrentHashMap<String, ConfirmerData> unconfirmed, Sender sender){
        myPercentLost=percentLost;
        myName=name;
        myDatagramSocket=datagramSocket;
        myRecentReceived=recentReceived;
        myUnconfirmed =unconfirmed;
        myNeighbours=neighbours;
        mySender=sender;
    }

    private void receive(DatagramPacket myDatagramPacket){
        //имитируем потерю пакета
        int randomPercentLost = new Random().nextInt(100);
        if (randomPercentLost < myPercentLost ) {
            return;
        }

        //декодируем байты в массив строк и определяем количество
        byte[] fullData = myDatagramPacket.getData();
        String[] messageStrings = decode (fullData);
        int numberOfStrings = messageStrings.length;

        //определяем кто послал текущее сообщение
        String senderAddress = myDatagramPacket.getAddress().getHostAddress();
        int senderPort = myDatagramPacket.getPort();
        String fullSenderAddress=senderAddress+":"+senderPort;

        if(numberOfStrings> Constants.MIN_SIZE_PACKET){
            //определеяем uuid собщения, header и его владельца
            String uuid=messageStrings[0];
            String messageHeader=messageStrings[1];
            String senderName=messageStrings[2];
            //определяем тип сообщения
            switch (messageHeader){
                case Constants.CHILD_HEADER:{
                    //получем текущий список соседей
                    if(!myNeighbours.contains(fullSenderAddress)){
                        myNeighbours.add(fullSenderAddress);
                        sendConfirmation(uuid,fullSenderAddress);
                        System.out.println(myName+": "+senderName + " " + fullSenderAddress + " connects.");
                    }
                    break;
                }
                case Constants.MESSAGE_HEADER:{
                    if(myRecentReceived.get(uuid) == null) {
                        myRecentReceived.putIfAbsent(uuid,new Date());
                        //кладем полученное сообщение в очерель сообщений
                        mySender.putMessageToQueue(uuid, Constants.MESSAGE_HEADER, messageStrings[3], senderName, fullSenderAddress);

                        //передаем строки для отображения в UI
                        System.out.println(senderName+": " +messageStrings[3]);
                        sendConfirmation(uuid, fullSenderAddress);
                    }
                    break;
                }
                case Constants.CONFIRMATION_HEADER:{
                    myUnconfirmed.get(messageStrings[3]).confirm(fullSenderAddress);
                    break;
                }
                default:{
                    System.out.println("Something wrong in receiving");
                }
            }
        }

    }

    private void sendConfirmation(String uuid, String fullReceiverAddress)
    {
        Message myMessage=new Message(UUID.randomUUID().toString(), Constants.CONFIRMATION_HEADER,uuid, myName,null);
        byte[] buffer = myMessage.toString().getBytes();
        String[] receiverStringsAddress = fullReceiverAddress.split(":");
        InetSocketAddress receiverAddress = new InetSocketAddress(receiverStringsAddress[0], Integer.parseInt(receiverStringsAddress[1]));
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress);
        try {
            myDatagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            //считываем наш пакет в буфер
            byte[] buffer = new byte[4096];
            DatagramPacket myDatagramPacket = new DatagramPacket(buffer, 4096);
            try {
                myDatagramSocket.receive(myDatagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            receive(myDatagramPacket);
        }
    }


    private String[] decode(byte[] encodedArray) {
        return new String(encodedArray, UTF_8).split("\n");
    }
}
