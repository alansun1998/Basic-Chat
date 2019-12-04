package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain extends Observable
{
	private ArrayList<PrintWriter> clientOutputStreams;
	public static ArrayList<Profile> users = new ArrayList<Profile>();	//	list to store users
	static Observable o;


	public static void main(String[] args) {    //	starts new networking thingy
		try {
			o = new Observable();
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
		System.out.println("waiting for connection");
		while (true) {
			Socket clientSocket = serverSock.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientOutputStreams.add(writer);
			Thread t = new Thread(new ClientHandler(clientSocket));
			//ClientHandler newClientHandler = new ClientHandler(clientSocket);
			t.start();
			System.out.println("got a connection");
		}
	}


	/**
	 * I have no idea what this does.
	 * @param message
	 */
	private void notifyClients (String message)
	{
		for (PrintWriter writer : clientOutputStreams) {
			writer.println(message);
			writer.flush();
		}
	}

	//	methods for working with user profiles
	public static ArrayList<Profile> getUsers(){
		return users;
	}
	public static void addUsers (Profile curr){
		users.add(curr);
		for(Profile x: users){
			System.out.print(x.username + " ");
		}
		System.out.println();
	}
	public void removeUsers (Profile rm){
		users.remove(rm);
	}
	public Profile findUser (String name){
		for (Profile x : users) {
			if (x.username.equals(name)) {
				return x;
			}
		}
		return null;
	}
	public static Observable getObs(){
		return o;
	}

	/*
	 * prints message to all clients when it receives one
	 */
	class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket clientSocket;
		boolean userNameAccept;

		public ClientHandler(Socket clientSocket) throws IOException {
			this.clientSocket = clientSocket;
			userNameAccept = false;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null)
				{
					/*if(!userNameAccept)
					{
						//addUsers(new Profile(message, clientSocket));
						System.out.println("new user: "+message);
						userNameAccept = true;
					}
					else*/
					{
						System.out.println("read " + message);
						setChanged();
						notifyClients(message);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
