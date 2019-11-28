package assignment7;

import java.io.IOException;
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Observable
{
	@SuppressWarnings("resource")	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Opening connection");
		ServerSocket ssock = new ServerSocket(4000);
		while(true)
		{
			Socket csock = ssock.accept();
			System.out.println("Got a connection");
		}
	}
}
