package assignment7;

public class Message {
    Profile sender;
    String msg;
    public Message(String in){
        sender = null;
        msg = in;
    }
    public Message(Profile x, String in){
        sender = x;
        msg = in;
    }
}
