package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.INotificationClient;

public class NotificationServer extends UnicastRemoteObject implements INotificationServer {

	
	private static final long serialVersionUID = 1L;
	private List<String> clientURLList;

	public NotificationServer() throws RemoteException {
		super();
		this.clientURLList = new ArrayList<>();
	}

	@Override
	public void subscribe(int port, String name) throws RemoteException {
		clientURLList.add(URLBuilder.buildURL(port, name));
	}

	@Override
	public void broadcastToAll(String message) throws RemoteException {
		INotificationClient client;
		for (String url : clientURLList) {
			try {
				client = (INotificationClient) Naming.lookup(url);
				client.display(message);
			} catch (MalformedURLException | NotBoundException e) {
				e.printStackTrace();
			}
		}
	}

}
