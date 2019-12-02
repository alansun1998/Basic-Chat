package assignment7;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
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

    public ChatRoom(){
        GridPane chat_Pane = new GridPane();
        chat_Pane.setPadding(new Insets(5));
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
                new newChatCreate(ClientMain.currentUser);
            }
        });


        chat_Pane.add(hist, 0, 0);
        chat_Pane.add(outgoing,0,1);
        chat_Pane.add(send,1,1);
        chat_Pane.add(newChat,1,0);
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
