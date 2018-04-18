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
import java.sql.Timestamp;
import java.util.Objects;

import app.ClientGUI;
import common.Article;
import server.INotificationServer;

public class NotificationClient extends UnicastRemoteObject implements INotificationClient, Runnable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final INotificationServer serverProxy;
	private final INotificationClient clientProxy;
	private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private final ClientGUI gui;

	public NotificationClient(String name, int port, String serverURL, ClientGUI gui)
			throws RemoteException, MalformedURLException, NotBoundException {
		super();
		this.name = name;
		this.gui = gui;
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind(name, this);
		this.serverProxy = (INotificationServer) Naming.lookup(serverURL);
		this.clientProxy = (INotificationClient) Naming.lookup("rmi://localhost:" + port + "/" + name);
	}

	@Override
	public void display(String message) throws RemoteException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (Objects.isNull(gui)) 
			System.out.println("\nNotification from server: " + message);
		else {
			gui.getTextArea().setText("[server][" + timestamp + "]\n" + message);
		}
			
	}
	
	public String getName() {
		return name;
	}
	
	public INotificationServer getServerProxy() {
		return serverProxy;
	}
	
	public INotificationClient getClientProxy() {
		return clientProxy;
	}

	@Override
	public void run() {

		System.out.println("Welcome " + name + "!");

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
					
				case "post_article":
					try {
						postArticle();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
				case "get_article":
					try {
						getArticle();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
	
				default:
					System.out.println("\nUnknown action: " + action + ".\n");
					break;
					
			}
		}

	}
	
	private void getArticle() throws IOException {
		String title = null;
		System.out.print("Title of the article: ");
		title = br.readLine();
		Article foundedArticle = serverProxy.getArticle(this, title);
		if (Objects.isNull(foundedArticle))
			System.out.println("\nError: article not found or you are not a subscriber.\n");
		else 
			System.out.println("\n" + foundedArticle + "\n");
	}
	
	private void postArticle() throws IOException {
		String title = null;
		String content = null;
		System.out.print("Title: ");
		title = br.readLine();
		System.out.print("Content: ");
		content = br.readLine();
		serverProxy.postArticle(this, new Article(title, name, content));
	}
	
	private void help() {
		System.out.println("\n" + "Here is the list of action you can do:\n"
				+ "- 'subscribe' to subscribe to the notification server,\n"
				+ "- 'unsubscribe' to unsubscribe to the notification server,\n"
				+ "- 'post_article' to post a new article,\n"
				+ "- 'get_article' to get an article.\n");
	}

	private void subscribe() {
		try {
			serverProxy.subscribe(name, clientProxy);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
