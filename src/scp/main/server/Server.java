package scp.main.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import scp.main.serverconn.ServerConnection;
import scp.main.serverconn.User;
import NetworkEncoder.java;

public class Server {
	private final Map<Socket, User> users = new HashMap<>();

	public void receiveMessage(String message, ServerConnection connection, Socket socket) {

		ServerSocket s1=new ServerSocket(/*socket number the client provides*/);
		
		Socket ss=s1.accept();
		//accepts the incoming request from client

		Scanner sc=new Scanner(ss.getInputStream());
		//accept the message which clients want to pass
	
		message=sc.nextLine();
		//reads the message
		
		PrintStream p=new PrintStream(ss.getOutputStream())
		//pass the message to client

		p.println(message)
		//prints the result


		// TODO Handle a message that a client has sent to you.
	}

	public void handleNewConnection(ServerConnection connection, Socket newConnection) {
		// TODO Handle an incoming connection. You will want to keep track of it by
		// making a User object to represent it and storing that user object in the
		// "users" map		
		// You will need to read the message that the client has sent to get the
		// username. Use the NetworkEncoder class's static functions to do this.
	

		User user=new User(connection,newConnection)
		users.put(newConnection, user)


		String a = pollmessage(newConnection);
		System.out.println(a);
	
	}
}
