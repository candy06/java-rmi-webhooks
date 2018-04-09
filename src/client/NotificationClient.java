package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.INotificationServer;

public class NotificationClient extends UnicastRemoteObject implements INotificationClient, Runnable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final INotificationServer serverProxy;
	private final INotificationClient clientProxy;

	public NotificationClient(String name, int port, String serverURL)
			throws RemoteException, MalformedURLException, NotBoundException {
		super();
		this.name = name;
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(name, this);
		this.serverProxy = (INotificationServer) Naming.lookup(serverURL);
		this.clientProxy = (INotificationClient) Naming.lookup("rmi://localhost:" + port + "/" + name);
	}

	@Override
	public void display(String message) throws RemoteException {
		System.out.println("\nNotification from server: " + message);
	}

	@Override
	public void run() {

		System.out.println("Welcome " + name + "!");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			
			System.out.print("Action (type 'help' to have the list of actions): ");
			
			String action = null;
			try {
				action = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			switch (action.toLowerCase()) {

				case "help":
					help();
					break;
	
				case "subscribe":
					subscribe();
					break;
	
				case "unsubscribe":
					unsubscribe();
					break;
	
				default:
					System.out.println("\nUnknown action: " + action + ".\n");
					break;
					
			}
		}

	}
	
	private void help() {
		System.out.println("\n" + "Here is the list of action you can do:\n" + "- 'quit' to quit,\n"
				+ "- 'subscribe' to subscribe to the notification server,\n"
				+ "- 'unsubscribe' to unsubscribe to the notification server.\n");
	}

	private void subscribe() {
		try {
			serverProxy.subscribe(name, clientProxy);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println(
				"\nYou are now a subscriber of the notification server and you will receive every notification from the server.\n");
	}

	private void unsubscribe() {
		try {
			serverProxy.unsubscribe(name, clientProxy);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println(
				"\nYou are not a subscriber anymore and thus, you won't receive any notification from the server.\n");
	}

}
