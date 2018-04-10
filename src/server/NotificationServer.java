package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import client.INotificationClient;
import common.Article;

public class NotificationServer extends UnicastRemoteObject implements INotificationServer {

	
	private static final long serialVersionUID = 1L;
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

	@Override
	public void postArticle(Article article) throws RemoteException {
		if (Objects.isNull(article)) return;
		articleList.add(article);
		System.out.println("\nNew article posted by: " + article.getAuthor() + "!\n");
		broadcastToAll("\nA new article has been posted by " + article.getAuthor() + "."
				+ "\nThe article name is '" + article.getTitle() + "'.\n");
	}

}
