/* CHAT ROOM <MyClass.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Christopher Saenz
 * cgs2258
 * 16185
 * Alan Sun
 * as79972
 * 16180
 * Slip days used: 1
 * Fall 2019
 */

package assignment7;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.TextArea;

public class Profile implements Observer
{
    String username;
    Socket sock;
    TextArea chat_feed = new TextArea();
    ArrayList<Profile> otherUsers = new ArrayList<Profile>();

//    public Profile(String name) {
//        this.username = name;
//    }

    public Profile(String name, Socket psock) throws IOException {
        this.username = name;
        this.sock = psock;
//        System.out.println("added user "+name+" (Total users: "+ServerMain.getUsers().size()+")");
    }

    @Override
    public String toString(){
        return username;
    }

	@Override
	public void update(Observable o, Object arg)
	{
		if(arg.getClass().equals(String.class)) {chat_feed.appendText(arg.toString());}
		else if(arg.getClass().equals(Profile.class))
		{
			if(!(otherUsers.contains(arg))) {otherUsers.add((Profile) arg);}
		}
	}
}
