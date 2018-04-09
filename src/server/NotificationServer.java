package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import client.INotificationClient;

public class NotificationServer extends UnicastRemoteObject implements INotificationServer {

	
	private static final long serialVersionUID = 1L;
	private List<INotificationClient> clientProxyList;

	public NotificationServer() throws RemoteException {
		super();
		this.clientProxyList = new ArrayList<>();
	}

	@Override
	public void subscribe(String name, INotificationClient clientProxy) throws RemoteException {
		if (Objects.isNull(clientProxy)) return;
		clientProxyList.add(clientProxy);
		System.out.println("\nNew subscriber: " + name + ".");
	}
	
	@Override
	public void unsubscribe(String name, INotificationClient clientProxy) throws RemoteException {
		if (Objects.isNull(clientProxy) || !clientProxyList.contains(clientProxy)) return;
		clientProxyList.remove(clientProxy);
		System.out.println("\n" + name + " is not a subscriber anymore.");
	}

	@Override
	public void broadcastToAll(String message) throws RemoteException {
		if (clientProxyList.isEmpty()) return;
		for (INotificationClient clientProxy : clientProxyList) {
			clientProxy.display(message);
		}
	}

}
