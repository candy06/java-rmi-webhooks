package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.INotificationClient;

public interface INotificationServer extends Remote {
	
	public void subscribe(String name, INotificationClient clientProxy) throws RemoteException;
	
	public void unsubscribe(String name, INotificationClient clientProxy) throws RemoteException;
	
	public void broadcastToAll(String message) throws RemoteException;

}
