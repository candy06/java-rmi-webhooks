package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.INotificationServer;

public class NotificationClient extends UnicastRemoteObject implements INotificationClient {

	private static final long serialVersionUID = 1L;
	private String name;
	private int port;
	private INotificationServer server;

	public NotificationClient(String name, int port, String serverURL) throws RemoteException {
		super();
		this.name = name;
		this.port = port;
		try {
			this.server = (INotificationServer) Naming.lookup(serverURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		server.subscribe(port, name);
	}

	@Override
	public void display(String message) throws RemoteException {
		System.out.println("New notification: " + message);
	}

}
