package assignment7;

import java.io.*;
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
	Socket mysock;				//	our socket
	PrintWriter writer;			//	our printwriter
	BufferedReader reader;		//	our bufferedreader
	TextArea incoming;			//	common textarea for incoming text
	Label connectstatus = new Label("...");
//	static Profile currentUser;	//	our user profile
	static String ip;			//	our ip address

	
	

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
		connectstatus.setText("Connecting...");
		while(!(mysock.isConnected())) {continue;}
		connectstatus.setText("Connected!");
	}

	
	
	/**
	 * Initializing method, makes connect window
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		Stage connect_Stage = new Stage();

		//	COMPONENTS OF STARTUP
		
		Pane connect_Pane2 = new HBox(5);
		connect_Pane2.setPadding(new Insets(5));
		GridPane connect_Grid = new GridPane();
//		connect_Grid.add(connect_Pane1,0,0);
		connect_Grid.add(connect_Pane2,0,1);
		connect_Grid.add(connectstatus,0,2);
		
		//	COMPONENTS OF connect_Pane
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
					connectstatus.setText("Searching...");
					connect(ip);
					new ChatRoom(new Profile("NO_NAME",mysock));
					connect_Stage.close();
				}
				catch(Exception excep) {connectstatus.setText(excep.getMessage());
				}
			}
		});
		connect_Pane2.getChildren().addAll(connect_Label,connect_IPaddress,connect_OK);

		incoming = new TextArea();
		incoming.setWrapText(true);
		incoming.setEditable(false);

		//	ADDING COMPONENTS TO STAGE
		connect_Stage.setScene(new Scene(connect_Grid));
		connect_Stage.show();
	}

//	public static Profile getCurrentUser(){
//		return currentUser;
//	}

	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * runnable class for reading incoming text and printing it to client chat
	 */
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
