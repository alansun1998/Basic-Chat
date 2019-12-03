package assignment7;

import javafx.beans.value.ObservableValueBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ChatRoom extends Stage implements Observer {

    Socket mysock;
    PrintWriter writer;
    BufferedReader reader;
    TextArea chat_Feed = new TextArea();
    TextArea incoming;
    Boolean global;

    public ChatRoom(Boolean isGlobal){
        global = isGlobal;
        GridPane chat_Pane = new GridPane();
        chat_Pane.setPadding(new Insets(5));
        GridPane menu = new GridPane();
        menu.setPadding(new Insets(5));
        
        try{
            connect(ClientMain.ip);
        }catch(Exception excep) {}
        incoming = new TextArea();
        incoming.setWrapText(true);
        incoming.setEditable(false);
        ScrollPane hist = new ScrollPane(incoming);

        TextArea outgoing = new TextArea();
        outgoing.setWrapText(true);

        Button send = new Button("Send");
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                writer.println(ClientMain.getCurrentUser().username + ": " + outgoing.getText());
                writer.flush();
                outgoing.setText("");;
                outgoing.requestFocus();
            }
        });

        Button newChat = new Button("New Chat");
        newChat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("HERE1");
                new newChatCreate(ClientMain.currentUser);
                System.out.println("HERE2");
            }
        });
        ChoiceBox<String> userList = new ChoiceBox<String>();
        try{
            for(Profile c: ServerMain.getUsers()){
                userList.getItems().add(c.username);
            }
        }catch (NullPointerException e){
            userList.getItems().add("There are no users :(");
        }
        if(ServerMain.getUsers().size() == 1)
            userList.getItems().add("There are no users :(");
        Button addFriend = new Button("Add Friend");
        addFriend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientMain.currentUser.addFriend(ServerMain.findUser(userList.getValue()));
            }
        });

        chat_Pane.add(hist, 0, 0);
        chat_Pane.add(outgoing,0,1);
        menu.add(send,0,1);
        menu.add(newChat,0,2);
        menu.add(addFriend,1,0);
        menu.add(userList,0,0);
        menu.setAlignment(Pos.CENTER);
        chat_Pane.add(menu,1,1);
        hist.setFitToWidth(true);
        chat_Pane.setConstraints(hist,0,0,2,1);
        if(global){
            this.setTitle(ClientMain.currentUser.username + "'s Global Chat");
        }
        else{
            this.setTitle(ClientMain.currentUser.username + "'s New Chat");
        }
        this.setScene(new Scene(chat_Pane));
        this.show();
    }

    public void connect(String ip) throws IOException
    {
        mysock = new Socket(ip,4000);
        writer = new PrintWriter(mysock.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
        Thread readerThread = new Thread(new ChatRoom.IncomingReader());
        readerThread.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        writer.println(arg);
        writer.flush();
    }

    class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    incoming.insertText(incoming.getLength(),message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
