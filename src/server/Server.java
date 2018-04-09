package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	
	private static String name = "notification_server";

	public static void main(String[] args) throws RemoteException {
		
		NotificationServer server = new NotificationServer();
		
		try {
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.rebind(name, server);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println("Server listening on port 8080...");
		
		while (true) {
			System.out.print("Enter a message: ");
			String msg = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				msg = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			server.broadcastToAll(msg);
		}
		
	}

}
