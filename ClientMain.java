package assignment7;

import java.io.*;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientMain extends Application
{
	static Socket mysock;
	static PrintWriter send;
	static BufferedReader reader;
	TextArea chat_Feed = new TextArea();
	
	
	
	/**
	 * Method for initializing connection to server
	 * @throws IOException
	 */
	public static void connect(String ip) throws IOException
	{
		mysock = new Socket(ip,4000);
		send = new PrintWriter(mysock.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(mysock.getInputStream()));
	}

	
	
	/**
	 * Initializing method
	 */
	@Override
	public void start(Stage chat_Stage) throws Exception
	{
		Stage connect_Stage = new Stage();
		Stage chatChoose_Stage = new Stage();
		
		
		//	PANES
		Pane connect_Pane = new HBox(5);
		connect_Pane.setPadding(new Insets(5));
		
		GridPane chat_Pane = new GridPane();
		chat_Pane.setPadding(new Insets(5));
		
		
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
				String ip = connect_IPaddress.getText();
				if(ip == "") {ip = "127.0.0.8";}
				try
				{
					connect(ip);
					connect_Stage.close();
					chat_Stage.show();
				}
				catch(Exception excep) {connect_IPaddress.setText(excep.getMessage());}
			}
		});
		connect_Pane.getChildren().addAll(connect_Label,connect_IPaddress,connect_OK);
		
		
		//	COMPONENTS OF chat_Pane
		chat_Feed.getPrefColumnCount(2);
		chat_Pane.add(chat_Feed, 0, 0);
		
		
		//	ADDING COMPONENTS TO STAGE
		connect_Stage.setScene(new Scene(connect_Pane));
		connect_Stage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
