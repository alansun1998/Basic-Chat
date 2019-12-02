package assignment7;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Profile {
    String username;
    Socket sock;
    public static ArrayList<Profile> friends = new ArrayList<Profile>();

    public Profile(String name){
        this.username = name;
    }

    public Profile(String name, Socket psock) throws IOException {
        this.username = name;
        this.sock = psock;
        ServerMain.addUsers(this);
    }

    public void addFriend(Profile fren){
        this.friends.add(fren);
    }
}
