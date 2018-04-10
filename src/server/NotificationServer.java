package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import client.INotificationClient;
import common.Article;

public class NotificationServer extends UnicastRemoteObject implements INotificationServer, Runnable {

	
	private static final long serialVersionUID = 1L;
	private static final String SERVER_URL = "notification_server";
	private static final int SERVER_PORT = 8080;
	
	private List<INotificationClient> clientProxyList;
	private List<Article> articleList;

	public NotificationServer() throws RemoteException {
		super();
		this.clientProxyList = new ArrayList<>();
		this.articleList = new ArrayList<>();
	}

	@Override
	public void subscribe(String name, INotificationClient clientProxy) throws RemoteException {
		if (Objects.isNull(clientProxy)) return;
		clientProxyList.add(clientProxy);
		System.out.println(">>> New subscriber: " + name + ".");
	}
	
	@Override
	public void unsubscribe(String name, INotificationClient clientProxy) throws RemoteException {
		if (Objects.isNull(clientProxy) || !clientProxyList.contains(clientProxy)) return;
		clientProxyList.remove(clientProxy);
		System.out.println(">>> " + name + " is not a subscriber anymore.");
	}

	@Override
	public void broadcastToAll(String message) throws RemoteException {
		if (clientProxyList.isEmpty()) return;
		for (INotificationClient clientProxy : clientProxyList) {
			clientProxy.display(message);
		}
	}

	@Override
	public void postArticle(INotificationClient client, Article article) throws RemoteException {
		if (Objects.isNull(article)) return;
		if (!clientProxyList.contains(client)) return;
		articleList.add(article);
		System.out.println(">>> New article posted by: " + article.getAuthor() + "!\n");
		broadcastToAll("\nA new article has been posted by " + article.getAuthor() + "."
				+ "\nThe article name is '" + article.getTitle() + "'.\n");
	}

	@Override
	public Article getArticle(INotificationClient client, String title) throws RemoteException {
		if (articleList.isEmpty() || Objects.isNull(title)) return null;
		if (!clientProxyList.contains(client)) return null;
		for (Article article : articleList) {
			if (article.getTitle().equals(title))
				return article;
		}
		return null;
	}

	@Override
	public void run() {
		
		try {
			Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
			registry.rebind(SERVER_URL, this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.out.println("Notification server ready!\n");
		
		while (true) {
			System.out.print("Write a notification for all the subscribers: ");
			String msg = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				msg = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				broadcastToAll(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}		
	}

}
