package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Observable
{
	private ArrayList<PrintWriter> clientOutputStreams;
	public static ArrayList<Profile> users = new ArrayList<Profile>();

	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		clientOutputStreams = new ArrayList<PrintWriter>();
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4000);
		InetAddress local = InetAddress.getLocalHost();
		System.out.println("IP Adresss: " + local.getHostAddress());
		while (true) {
			Socket clientSocket = serverSock.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientOutputStreams.add(writer);
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			System.out.println("got a connection");
		}
	}
	private void notifyClients(String message) {
		for (PrintWriter writer : clientOutputStreams) {
			writer.println(message);
			writer.flush();
		}
	}

	public static ArrayList<Profile> getUsers(){
	    return users;
    }

    public static void addUsers(Profile curr){
	    users.add(curr);
	    System.out.println(users);
    }

    public static Profile findUser(String name){
		for(Profile x: users){
			if(x.username.equals(name)){
				return x;
			}
		}
		return null;
	}

	class ClientHandler implements Runnable {
		private BufferedReader reader;

		public ClientHandler(Socket clientSocket) throws IOException {
			Socket sock = clientSocket;
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					setChanged();
					notifyClients(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
