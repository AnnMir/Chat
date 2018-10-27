import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Listener implements Runnable {

    private Sender mySender;

    Listener(Sender sender){
        mySender = sender;
        this.run();
    }

    @Override
    public void run() {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String msg;
        while (true) {
            try {
                //System.out.println("Enter your message");
                msg = userInput.readLine();
                if(!"".equals(msg)) {
                    mySender.putMessageToQueue(null, Constants.MESSAGE_HEADER,msg,null,null);
                    System.out.println(mySender.getMyName()+": "+msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
