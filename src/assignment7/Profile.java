package assignment7;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Profile implements Observer {
    String username;
    Socket sock;

//    public Profile(String name) {
//        this.username = name;
//    }

    public Profile(String name, Socket psock) throws IOException {
        this.username = name;
        this.sock = psock;
        ServerMain.addUsers(this);
        System.out.println("added user "+name+" (Total users: "+ServerMain.getUsers().size()+")");
    }

    @Override
    public String toString(){
        return username;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
