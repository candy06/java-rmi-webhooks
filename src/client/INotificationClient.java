package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INotificationClient extends Remote {

	public void display(String message) throws RemoteException;
	
}
