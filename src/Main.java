import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        String nodeName=args[0];
        int nodePercentLost=Integer.parseInt(args[1]);
        int nodePort=Integer.parseInt(args[2]);

        if(args.length==3){
            Node node=new Node(nodeName,nodePercentLost,nodePort);
            node.nodeCreate();

        }
        else if(args.length==5){
            String nodeParentIp=args[3];
            int nodeParentPort=Integer.parseInt(args[4]);
            Node node;
            try {
                node = new Node(nodeName,nodePercentLost,nodePort,InetAddress.getByName(nodeParentIp),nodeParentPort);
                node.nodeCreate();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        else JOptionPane.showMessageDialog(null, "Wrong arguments");
    }
}