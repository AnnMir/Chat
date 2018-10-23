import java.io.Serializable;
import java.util.UUID;

public class GUID implements Serializable{

    private static String ID;

    GUID(){
        UUID uuid = UUID.randomUUID();
        ID = uuid.toString();
    }

    public String getID(){
        return ID;
    }

}

