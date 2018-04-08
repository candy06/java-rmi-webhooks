package server;

public class URLBuilder {
	
	public static String buildURL(int port, String name) {
		return "rmi://localhost:" + port + "/" + name;
	}

}
