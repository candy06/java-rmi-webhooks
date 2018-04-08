package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INotificationServer extends Remote {
	
	public void subscribe(int port, String name) throws RemoteException;
	
	public void broadcastToAll(String message) throws RemoteException;

}
