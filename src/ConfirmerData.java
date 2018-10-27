import java.util.Set;

public class ConfirmerData{
    private String data;
    private Set<String> myNeighbours;
    private int resendingCounter;
    private String prevSender;

    public ConfirmerData(String data, Set<String> neighbours,String prev)
    {
        this.prevSender=prev;
        this.data = data;
        this.myNeighbours = neighbours;
    }

    public String getData()
    {
        resendingCounter++;
        return data;
    }

    public int getResendingCounter()
    {
        return resendingCounter;
    }

    public boolean isEmpty()
    {
        return myNeighbours.isEmpty();
    }

    public Set<String> getMyNeighbours(){
        return myNeighbours;
    }

    public void confirm(String senderAddress)
    {
        myNeighbours.remove(senderAddress);
    }

    public String getPrevSender() {
        return prevSender;
    }
}
