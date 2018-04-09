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
	public void subscribe(INotificationClient clientProxy) throws RemoteException {
		if (Objects.isNull(clientProxy)) return;
		clientProxyList.add(clientProxy);
	}

	@Override
	public void broadcastToAll(String message) throws RemoteException {
		for (INotificationClient clientProxy : clientProxyList) {
			clientProxy.display(message);
		}
	}

}
