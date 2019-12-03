package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class ServerMain extends Observable
{
	private ArrayList<PrintWriter> clientOutputStreams;
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
		InetAddress myIP = InetAddress.getLocalHost();
		System.out.println("Host Address: "+myIP.getHostAddress());
		System.out.println("waiting for connection");
		while (true) {
			Socket clientSocket = serverSock.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientOutputStreams.add(writer);
			ClientHandler newClientHandler = new ClientHandler(clientSocket);
			Thread t = new Thread(newClientHandler);
			t.start();
			System.out.println("got a connection");
		}

	}
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
					if(!userNameAccept)
					{
						addUsers(new Profile(message, clientSocket));
						System.out.println("new user: "+message);
						userNameAccept = true;
					}
					else
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
