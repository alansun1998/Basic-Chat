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
	Socket mysock;
	PrintWriter writer;
	BufferedReader reader;
	TextArea chat_Feed = new TextArea();
	TextArea incoming;

	
	
	
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
		//Stage chat_Stage = new Stage();
		
		
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
				writer.println(outgoing.getText());
				writer.flush();
				outgoing.setText("");;
				outgoing.requestFocus();

			}
		});

		chat_Feed.setPrefColumnCount(2);
		chat_Pane.add(hist, 0, 0);
		chat_Pane.add(outgoing,0,1);
		chat_Pane.add(send,1,1);
		
		
		//	ADDING COMPONENTS TO STAGE
		connect_Stage.setScene(new Scene(connect_Pane));
		connect_Stage.show();
		chat_Stage.setScene((new Scene(chat_Pane)));
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
