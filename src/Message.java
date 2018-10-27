public class Message {
    private String uuid;
    private String header;
    private String message;
    private String senderName;
    private String nameFrom;

    public Message(String uuid,String head, String message,String name,String from) {
        this.uuid = uuid;
        this.header=head;
        this.message = message;
        this.senderName=name;
        this.nameFrom=from;
    }

    @Override
    public String toString() {
        return uuid + "\n" + header+ "\n" + senderName + "\n" + message + "\n";
    }

    public String getUuid() {
        return uuid;
    }

    public String getNameFrom() {
        return nameFrom;
    }
}