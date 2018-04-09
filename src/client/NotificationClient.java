package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.INotificationServer;

public class NotificationClient extends UnicastRemoteObject implements INotificationClient {

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
		serverProxy.subscribe(clientProxy);
	}

	@Override
	public void display(String message) throws RemoteException {
		System.out.println("[" + name + "] - New notification from server: " + message);
	}	

}
