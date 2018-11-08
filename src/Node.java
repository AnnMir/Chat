import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private String myName;
    private InetAddress myParentIp;
    private int myParentPort;
    private int myPort;
    private int myPercentLost;
    private boolean isRoot;
    private DatagramSocket myDatagramSocket;
    private static final int RESENDING_PERIOD = 3000;

    Node(String name,int percentLost, int port){
        myName =name;
        myPort=port;
        myPercentLost=percentLost;
        isRoot=true;
    }

    Node(String name,int percentLost, int port, InetAddress parentIp,int parentPort){
        myName =name;
        myPort=port;
        myPercentLost=percentLost;
        myParentPort=parentPort;
        myParentIp=parentIp;
        isRoot=false;
    }

    public void nodeCreate(){
        Sender mySender;
        Receiver myReceiver;
        Confirmer myConfirmer;
        ConcurrentHashMap<String, ConfirmerData> unconfirmed=new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Date> recentReceived=new ConcurrentHashMap<>();
        Set<String> myNeighbours = ConcurrentHashMap.newKeySet();

        try {
            myDatagramSocket=new DatagramSocket(myPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if(isRoot) {
            mySender = new Sender(myName,myDatagramSocket,unconfirmed, myNeighbours);
        }
        else {
            mySender=new Sender(myName,myDatagramSocket,myParentIp,myParentPort,unconfirmed, myNeighbours);
        }

        myConfirmer=new Confirmer(myDatagramSocket,unconfirmed,recentReceived, myNeighbours);
        myReceiver=new Receiver(myName,myDatagramSocket,myPercentLost, myNeighbours,recentReceived,unconfirmed,mySender);


        new Thread(mySender).start();
        new Thread(myReceiver).start();
        new Timer().schedule(myConfirmer,1000,RESENDING_PERIOD);
        new Listener(mySender);
    }
}