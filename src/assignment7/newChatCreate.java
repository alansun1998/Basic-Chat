package assignment7;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class newChatCreate extends Stage {

    Stage newChat = new Stage();
    public newChatCreate(Profile currentUser){
        Pane friend_Pane = new HBox(5);
        friend_Pane.setPadding(new Insets(5));
        Label addFriend = new Label("Add person to chat: ");
        ChoiceBox<String> friendList = new ChoiceBox<String>();
        try{
            for(Profile c: currentUser.friends){
                friendList.getItems().add(c.username);
            }
        }catch (NullPointerException e){
            friendList.getItems().add("You have no friends :(");
        }
        if(currentUser.friends.size() == 0)
            friendList.getItems().add("You have no friends :(");
        Button add = new Button("Add");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new ChatRoom();
                newChat.close();
            }
        });
        friend_Pane.getChildren().addAll(addFriend,friendList,add);

        newChat.setScene(new Scene(friend_Pane));
        newChat.show();
    }
}
