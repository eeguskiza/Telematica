package tcpClient;

import util.Settings;

public class ClientApp {

	public static void main(String[] args) {
		// This is an example of a possible task using the client API to operate with the server

		TCPClient client = new TCPClient("127.0.0.1", Settings.PORT);

		client.validate("pepe", "pepe");

		String[] files = client.listfiles("/");
		for(int i=0;i<files.length;i++)
			System.out.println(files[i]);

		System.out.println(client.getInfo("/"));

		client.quit();
	}
}
