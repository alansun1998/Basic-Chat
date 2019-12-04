/* CHAT ROOM <MyClass.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Christopher Saenz
 * cgs2258
 * 16185
 * Alan Sun
 * as79972
 * 16180
 * Slip days used: 1
 * Fall 2019
 */

package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain extends Observable
{
	private ArrayList<PrintWriter> clientOutputStreams;
	public static ArrayList<Profile> users = new ArrayList<Profile>();	//	list to store users
	Rooms all = new Rooms();
	Rooms room1 = new Rooms();
	Rooms room2 = new Rooms();
	Rooms room3 = new Rooms();

	public static void main(String[] args) {    //	starts new networking thingy
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

	/*
	 * prints message to all clients when it receives one
	 */
	class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket clientSocket;

		public ClientHandler(Socket clientSocket) throws IOException {
			this.clientSocket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null)
				{
					String[] breakdown = message.split("#");
					if(breakdown[0].equals("ADD")){
						if(breakdown[1].equals("AllRM")){
							all.add(clientSocket);
						}
						else if(breakdown[1].equals("Room1")){
							room1.add(clientSocket);

						}
						else if(breakdown[1].equals("Room2")){
							room2.add(clientSocket);
						}
						else{
							room3.add(clientSocket);
						}
					}
					else if(breakdown[0].equals("AllRM")){
						System.out.println("read " + breakdown[1]);
						setChanged();
						all.notifyClients(breakdown[1]);
					}
					else if(breakdown[0].equals("Room1")){
						System.out.println("read " + breakdown[1]);
						setChanged();
						room1.notifyClients(breakdown[1]);
					}
					else if(breakdown[0].equals("Room2")){
						System.out.println("read " + breakdown[1]);
						setChanged();
						room2.notifyClients(breakdown[1]);
					}
					else if(breakdown[0].equals("Room3")){
						System.out.println("read " + breakdown[1]);
						setChanged();
						room3.notifyClients(breakdown[1]);
					}
					else{
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
	class Rooms extends Observable{
		ArrayList<PrintWriter> roomOut;
		public Rooms(){
			this.roomOut = new ArrayList();
		}
		public void add(Socket x) throws IOException {
			this.roomOut.add(new PrintWriter(x.getOutputStream()));
		}
		private void notifyClients (String message)
		{
			for (PrintWriter writer : this.roomOut) {
				writer.println(message);
				writer.flush();
			}
		}




	}
}
