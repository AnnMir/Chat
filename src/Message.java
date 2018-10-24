import java.net.DatagramSocket;
import java.time.LocalTime;
import java.util.Map;

public class Message {
    private String message;

    public Message(String msg, String Type){
        GUID guid = new GUID();
        message = guid.getID()+" "+Type+" "+ LocalTime.now().toString()+" "+msg;
    }

    public Message(String msg){
        message = msg;
    }

    public Message(){}

    public String getMessage(String msg) {
        String[] tmp = msg.split(" ");
        msg.replaceFirst(tmp[0]+" "+tmp[1]+" "+tmp[2]+" ","");
        return msg;
    }
    public String getMessage(){
        return message;
    }

    public String getID(String msg){
        String[] tmp = msg.split(" ");
        return tmp[0];
    }

    public String getMessage(Message msg){
            return getMessage(msg.getMessage());
    }

    public boolean Control(Message msg,String id){
        if(!Main.SendingControl.isEmpty()){
            for(Map.Entry<Message,DatagramSocket> tmp: Main.getSendingControl().entrySet()){
                if(getID(msg.getMessage()).equals(id))
                    return true;
            }
        }
        return false;
    }

    public void DeleteControl(Message msg, String id){
        for(Map.Entry<Message,DatagramSocket> tmp: Main.getSendingControl().entrySet()){
            if(getID(msg.getMessage()).equals(id)){
                Main.getSendingControl().remove(tmp.getKey(),tmp.getValue());
            }
        }
    }

    public static void setControl(Message msg, DatagramSocket socket){
        Main.getSendingControl().put(msg,socket);
    }

    public String getType(String msg){
        String[] tmp = msg.split(" ");
        return tmp[1];
    }

    public String setType(String msg, String type){
        String[] tmp = msg.split(" ");
        tmp[1] = type;
        return msg.replaceFirst(tmp[1],type);
    }
    public LocalTime getTime(String msg){
        String[] tmp = msg.split(" ");
        return LocalTime.parse(tmp[2]);
    }
}
