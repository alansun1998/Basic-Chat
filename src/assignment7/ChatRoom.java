package assignment7;

import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * chat room window class
 */
public class ChatRoom extends Stage implements Observer {

    Socket mysock;
    PrintWriter writer;
    BufferedReader reader;
    ObjectOutputStream writer2;
    ObjectInputStream reader2;
    TextArea chat_Feed = new TextArea();
    Profile currentUser;
    TextArea incoming;
    Stage userStage = new Stage();
    String roomName;

    public ChatRoom(Profile sudoWorkPlease, String title)
    {
    	currentUser = sudoWorkPlease;
    	roomName = title;
    	//System.out.println(ServerMain.o);
    	//ServerMain.getObs().addObserver(this);
    	GridPane chat_Pane = new GridPane();
        chat_Pane.setPadding(new Insets(5));
        GridPane menu = new GridPane();
        menu.setPadding(new Insets(5));
        Stage actualChatStage = new Stage();

        try{
            connect(ClientMain.ip);
        }catch(Exception excep) {}

        incoming = new TextArea();
        incoming.setWrapText(true);
        incoming.setEditable(false);
        ScrollPane hist = new ScrollPane(incoming);

        TextField outgoing = new TextField();
        outgoing.setPromptText("Enter your message here!");

        Button send = new Button("Send");
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Message msg = new Message(currentUser.username + ": " + outgoing.getText());
                writer.println(roomName + "#" + currentUser.username + ": " + outgoing.getText());
                writer.flush();
                //writer2.writeObject(msg);
                //writer2.flush();
                outgoing.setText("");;
                outgoing.requestFocus();

            }
        });

        Button join = new Button("Join");
        join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //new newChatCreate(ClientMain.currentUser);
                for(Node x: chat_Pane.getChildren()){
                    x.setDisable(false);
                }
                writer.println("ADD#" + roomName);
                writer.flush();
            }
        });
        ChoiceBox<String> userList = new ChoiceBox<String>();
        try{
            for(Profile c: ServerMain.getUsers()){
                //if(!c.equals(currentUser))
                System.out.println(ServerMain.getUsers());
                    userList.getItems().add(c.username);
            }
        }catch (NullPointerException e){
            userList.getItems().add("There are no users :(");
        }
        if(ServerMain.getUsers().size() == 0)
            userList.getItems().add("There are no users :(");
        Button addFriend = new Button("Add Friend");
        addFriend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //ClientMain.currentUser.addFriend(ServerMain.findUser(userList.getValue()));
            }
        });

        chat_Pane.add(hist, 0, 0);
        chat_Pane.add(outgoing,0,1);
        menu.add(send,0,1);
        menu.add(addFriend,1,0);
        menu.add(userList,0,0);
        menu.setAlignment(Pos.CENTER);
        chat_Pane.add(menu,1,1);
        hist.setFitToWidth(true);
        chat_Pane.setConstraints(hist,0,0,2,1);
        actualChatStage.setTitle(roomName);
        if(!roomName.equals("AllRM")){
            menu.add(join,0,2);
            for(Node x:chat_Pane.getChildren()){
                x.setDisable(true);
            }
            menu.setDisable(false);
            join.setDisable(false);
        }
        actualChatStage.setScene(new Scene(chat_Pane));
        actualChatStage.show();
    }

    public void connect(String ip) throws IOException
    {
        mysock = new Socket(ip,4000);
        writer = new PrintWriter(mysock.getOutputStream());
        //writer2 = new ObjectOutputStream(mysock.getOutputStream());
        //writer2.flush();
        reader = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
        if(roomName.equals("AllRM")){
            writer.println("ADD#" + roomName);
            writer.flush();
        }
        //reader2 = new ObjectInputStream(mysock.getInputStream());
        Thread readerThread = new Thread(new ChatRoom.IncomingReader());
        readerThread.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("OHAI");
        writer.println(arg);
        writer.flush();
    }

    class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                //while ((message = ((Message) reader2.readObject()).msg) != null) {
                while ((message = reader.readLine()) != null) {
                    incoming.insertText(incoming.getLength(),message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
