package assignment7;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import com.sun.security.ntlm.Server;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientMain extends Application
{
	Socket mysock;
	PrintWriter writer;
	BufferedReader reader;
	TextArea chat_Feed = new TextArea();
	TextArea incoming;
	static Profile currentUser;
	static String ip;

	
	

	/**
	 * Method for initializing connection to server
	 * @throws IOException
	 */
	public void connect(String ip) throws IOException
	{
		mysock = new Socket(ip,4000);
		writer = new PrintWriter(mysock.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

	
	
	/**
	 * Initializing method
	 */
	@Override
	public void start(Stage chat_Stage) throws Exception
	{
		Stage connect_Stage = new Stage();

		//	COMPONENTS OF STARTUP
		Pane connect_Pane1 = new HBox(5);
		connect_Pane1.setPadding(new Insets(5));
		Pane connect_Pane2 = new HBox(5);
		connect_Pane2.setPadding(new Insets(5));
		GridPane connect_Grid = new GridPane();
		connect_Grid.add(connect_Pane1,0,0);
		connect_Grid.add(connect_Pane2,0,1);
		
		//	COMPONENTS OF connect_Pane
		Label username = new Label("Chat Username:");
		TextField username_Entry = new TextField();
		username_Entry.setPromptText("Enter chat Username");
		Label connect_Label = new Label("Connect to:");
		TextField connect_IPaddress = new TextField();
		connect_IPaddress.setPromptText("Enter IP address to connect to");
		Button connect_OK = new Button("CONNECT");
		connect_OK.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				ip = connect_IPaddress.getText();
				if(ip == "") {ip = "127.0.0.8";}
				try
				{
					connect(ip);
					currentUser = new Profile(username_Entry.getText(),mysock);
					Profile x = new Profile("testing",mysock);
					currentUser.addFriend(x);
					//currentUser.addFriend((new Profile("tesing2")));
					connect_Stage.close();
					new ChatRoom(true);
				}
				catch(Exception excep) {connect_IPaddress.setText(excep.getMessage());
				excep.printStackTrace();}
			}
		});
		connect_Pane1.getChildren().addAll(username,username_Entry);
		connect_Pane2.getChildren().addAll(connect_Label,connect_IPaddress,connect_OK);

		incoming = new TextArea();
		incoming.setWrapText(true);
		incoming.setEditable(false);

		//	ADDING COMPONENTS TO STAGE
		connect_Stage.setScene(new Scene(connect_Grid));
		connect_Stage.show();
	}

	public static Profile getCurrentUser(){
		return currentUser;
	}

	public static void main(String[] args)
	{
		launch(args);
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
