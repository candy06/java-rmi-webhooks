package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	
	private static final String SERVER_URL = "rmi://localhost:8080/notification_server";

	public static void main(String[] args) {
		
		String clientName = args[0];
		int port = Integer.parseInt(args[1]);
		
		try {
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind(clientName, new NotificationClient(clientName, port, SERVER_URL));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
