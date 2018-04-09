package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.INotificationClient;

public interface INotificationServer extends Remote {
	
	public void subscribe(INotificationClient clientProxy) throws RemoteException;
	
	public void broadcastToAll(String message) throws RemoteException;

}
