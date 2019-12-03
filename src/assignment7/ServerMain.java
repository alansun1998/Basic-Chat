package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain extends Observable
{
	private ArrayList<PrintWriter> clientOutputStreams;	//	idk what this does lololol
	public ArrayList<Profile> users = new ArrayList<Profile>();	//	list to store users


	public static void main(String[] args) {	//	starts new networking thingy
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
			ClientHandler newClientHandler = new ClientHandler(clientSocket);
			Thread t = new Thread(newClientHandler);
			t.start();
			System.out.println("got a connection");
			
			//	addUser in ClientMain doesn't actually add the user to the users arraylist, so i made this
//			int wait;
//			while((wait = System.in.read()) != -1) {continue;}
//			String newUserName = newClientHandler.reader.readLine();
//			addUsers(new Profile(newUserName, clientSocket));
//			System.out.println("got a connection: "+newUserName);
		}
	}
	private void notifyClients(String message) {
	/**
	 * I have no idea what this does.
	 * @param message
	 */
	private void notifyClients(String message)
	{
		for (PrintWriter writer : clientOutputStreams) {
			writer.println(message);
			writer.flush();
		}
	}

	//	methods for workign with user profiles
	public ArrayList<Profile> getUsers(){
	    return users;
    }
    public void addUsers(Profile curr){
	    users.add(curr);
	    System.out.println(users);
    }
    public void removeUsers(Profile rm){
    	users.remove(rm);
    }
    public Profile findUser(String name){
		for(Profile x: users){
			if(x.username.equals(name)){
				return x;
			}
		}
		return null;
	}

    /**
     * prints message to all clients when it receives one
     */
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
