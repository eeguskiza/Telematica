package tcpClient;

import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class TCPClient {

	private Socket so;
    private BufferedReader in;
    private PrintStream out;

	
	public TCPClient(String ip, int port) {
		try {
			so = new Socket(ip, port);
		    in = new BufferedReader(new InputStreamReader(so.getInputStream()));
		    out = new PrintStream(so.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public boolean validate(String user, String pass) {
		String reply;
		try {
			out.println("USER " + user);
			reply = in.readLine();
			out.println("PASS " + pass);
			reply = in.readLine();
			if(reply.startsWith("200")) return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public String[] listfiles(String path){
		ArrayList<String> al = new ArrayList<String>();
		try {
			out.println("LISTFILES " + path);
			String reply = in.readLine();
			for(;!reply.startsWith("201");){
				al.add(reply);			
				reply = in.readLine();			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] as = new String[al.size()];
		return al.toArray(as);
	}
	
	public String getInfo(String filename){
		try {
			out.println("GETINFO " + filename);
			String reply = in.readLine();
			if (reply.startsWith("402"))
				return null;
			else 
				return reply.substring(6);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public boolean quit(){
		try {
			out.println("QUIT");
			String reply = in.readLine();
			if (reply.startsWith("203")){
				in.close();
				out.close();
				so.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
