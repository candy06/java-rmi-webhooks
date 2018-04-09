package client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
	
	private static final String SERVER_URL = "rmi://localhost:8080/notification_server";

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		
		String name = args[0];
		int port = Integer.parseInt(args[1]);
		
		new Thread(new NotificationClient(name, port, SERVER_URL)).start();
		
		
		
	}

}
