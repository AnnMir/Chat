import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Confirmer extends TimerTask
{
    private ConcurrentHashMap<String, ConfirmerData> myUnconfirmed;
    private Set<String> myNeighbours;
    private ConcurrentHashMap<String, Date> myRecentReceived;
    private DatagramSocket myDatagramSocket;
    private static final int RESENDING_PERIOD = 3000;

    Confirmer(DatagramSocket datagramSocket, ConcurrentHashMap<String, ConfirmerData> unconfirmed
            ,ConcurrentHashMap<String, Date> recentReceived,Set<String> neighbours){
        myDatagramSocket=datagramSocket;
        myNeighbours= neighbours;
        myUnconfirmed = unconfirmed;
        myRecentReceived = recentReceived;
    }

    public void run()
    {
        //проходимся по всем неподтвержденным сообщениям
        for (Map.Entry<String, ConfirmerData> entry : myUnconfirmed.entrySet()) {
            if (entry.getValue().isEmpty())
                continue;

            Set<String> neighbours = entry.getValue().getMyNeighbours();
            byte[] buffer = (entry.getValue().getData()).getBytes();
            String prevSender=entry.getValue().getPrevSender();
            //повторно отсылаем это сообщение тем, кто не подтвердил его
            for (String address : neighbours) {
                if(address.equals(prevSender)){
                    entry.getValue().confirm(prevSender);
                    continue;
                }
                String[] receiverStringsAddress = address.split(":");
                InetSocketAddress receiverAddress = new InetSocketAddress(receiverStringsAddress[0]
                        , Integer.parseInt(receiverStringsAddress[1]));
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress);
                try {
                    System.out.println("try send "+entry.getKey()+" to "+address);
                    myDatagramSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //если число попыток равно 20, удаляем кто не подтвердил из соседей
            if (entry.getValue().getResendingCounter() == 20)
            {
                neighbours=entry.getValue().getMyNeighbours();
                for (String address : neighbours) {
                    myNeighbours.remove(address);
                    myUnconfirmed.remove(entry.getKey());
                    System.out.println("Neighbour " + address + " left.");
                }
            }
        }
        //очищаем список только что полученных сообщений
        Date now = new Date();
        for (Map.Entry<String, Date> entry : myRecentReceived.entrySet())
        {
            if(now.compareTo(entry.getValue()) > (RESENDING_PERIOD * 20))
                myRecentReceived.remove(entry.getKey());
        }
    }
}