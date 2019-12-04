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

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ObjectInputStream;

public class newChatCreate extends Stage {

    Stage newChat = new Stage();
    public newChatCreate(Profile currentUser){
        Pane privateChat_Pane = new VBox(5);
        privateChat_Pane.setPadding(new Insets(5));
        Label addFriend = new Label("Add person to chat: ");
        ChoiceBox<String> friendList = new ChoiceBox<String>();

        ArrayList<CheckBox> chooseChat = new ArrayList<CheckBox>();
        try{
            for(Profile c: ServerMain.getUsers()){
                if(!c.equals(currentUser))
                    chooseChat.add(new CheckBox(c.username));
            }
        }catch (NullPointerException e){
        	CheckBox filler = new CheckBox("no one's here lol :P");
        	filler.setDisable(true);
        	chooseChat.add(filler);
        }
//        if(currentUser.friends.size() == 0)
//            friendList.getItems().add("You have no friends :(");

        Button add = new Button("Add");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //new ChatRoom(null);
                newChat.close();
            }
        });
        privateChat_Pane.getChildren().addAll(addFriend,add); privateChat_Pane.getChildren().addAll(chooseChat);

        newChat.setScene(new Scene(privateChat_Pane));
        newChat.show();
    }
}
