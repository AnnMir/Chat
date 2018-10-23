import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalTime;

public class Message implements Serializable {
    private String message;
    private String ID;
    private InetAddress IP;
    private Integer Port;
    private String Type;
    private LocalTime Time;

    public Message(String msg, String type){
        this.message = msg;
        GUID guid = new GUID();
        this.ID = guid.getID();
        this.IP = Chat_Tree.getMyIP();
        this.Port = Chat_Tree.getOwnPort();
        this.Type = type;
        this.Time = LocalTime.now();
    }

    public String getMessage() {
        return this.message;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getID() {
        return this.ID;
    }

    public LocalTime getTime() {
        return Time;
    }
}
