/**
 * <p>Title: Example 2: Daemon</p>
 *
 * <p>Description: Daemon</p>
 *
 * <p>Copyright: Copyright (c) 2023</p>
 *
 * <p>Company: University of Deusto</p>
 *
 * @author Inaki Vzzquez
 * @version 1.1
 */

package tcpServer;

import java.io.IOException;
import java.net.ServerSocket;

import util.Settings;

public class DaemonStatefulServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Create a ServerSocket and listen for incoming connections
			ServerSocket ss = new ServerSocket(Settings.PORT);
			System.out.println("Daemon running...");
			for(;;){
				// Accept incoming connections and create child server to process each one
				StatefulTCPServer server = new StatefulTCPServer(ss.accept());
				server.start();			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
