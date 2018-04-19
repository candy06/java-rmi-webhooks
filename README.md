# Implementation of the webhooks protocol using Java RMI
> The goal of this project is to give an implementation of the _webhooks_ protocol. This protocol is mostly used in the WEB field and consists in a format _publisher/subscriber_. The idea is that a client call a distant web server (using its URL) and establish a connexion with the server. Moreover, the client gives its proxy through the subscribe method so that the server can communicate with him.

## The structure of my solution

There are four packages in which you have different things:
* **app** is a package that only have one class called _ClientGUI.java_. This class is used when you want to launch a new client. To do that you have to first put two program arguments: the first one is the name of the client and the second one is the port.
* **client** is a package in wich you have all the useful code for the client; an interface _INotificationClient.java_ that only has one method that display a message passed in parameter, a _NotificationClient.java_ class that implement the interface mentioned just before and add some usefull information like the proxies of the server/client, and finaly a _Client.java_ class that contains a main method to start a client (console) with, again, two program arguments (client name, port).
* **common** is a package that only contain one class, the _Article.java_ that represents an article that can be created by the subscribers (clients).
* **server** has the same structure as client but contains all the useful class for the server-side. However, the interface implemented has more methods: subscribe, unsubscribe, postArticle, getArticle and broadcastToAll.

## How to run my solution?

1. Launch the class *Server.java*.
2. Launch as many clients as you want - *ClientGUI.java* if you want to have a graphical one or *Client.java* for a console one. Don't forget to add the two program arguments (client name and port number).
3. At this point you can do some simulations: subscribe with a client, go to the server and write a message (on the console), the message will be displayed to the subscriber. Now you can add more subscribers and send another message to all the subscribers. Also, a subscriber can create an article by giving a title and a content. When the article is created, all the subscribers will have a notification. Now let's imagine I am a client and I want to read an article, I can do it whenever I want by typing the title of the article. And of course a client can unsubscribe and he will not receive any notification anymore.
4. If you are using the console client, you can type 'help' to have the list of all the commands available.
