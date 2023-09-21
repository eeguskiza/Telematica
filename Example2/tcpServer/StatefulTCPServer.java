/**
 * <p>Title: Example 2: StatefulTCPServer</p>
 *
 * <p>Description: Basic Stateful TCP server</p>
 *
 * <p>Copyright: Copyright (c) 2023</p>
 *
 * <p>Company: University of Deusto</p>
 *
 * @author Inaki Vzzquez
 * @version 1.1
 */

package tcpServer;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class StatefulTCPServer extends Thread{

		Socket socketToClient;
	    private BufferedReader in;
	    private PrintStream out;
	    
	    private Logger logger;
		
		public StatefulTCPServer(Socket s) throws IOException{			
			socketToClient = s;
		    in = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()));
		    out = new PrintStream(socketToClient.getOutputStream());
		    
		    logger = Logger.getLogger("Server");
		    logger.setLevel(Level.INFO);
		    logger.info("Connection from " + s.getInetAddress().getHostAddress());
		}
		
		// Implementation of a basic file management protocol
        public void run(){
        	List<String> commandList = Arrays.asList("USER", "PASS", "QUIT", "LISTFILES", "GETINFO", "user", "pass", "quit", "listfiles", "getinfo");
        	int state = 0;
        	String uname = "";
        	String message, command;
	        try{
	        	for(;state!=3;){
	        		message = in.readLine();
	        		
	        		logger.info("Received: " + message);

	        		StringTokenizer st = new StringTokenizer(message);
	        		command = st.nextToken();
	        		switch(state){
	        			case 0:
	        				if(command.equalsIgnoreCase("USER")){
	        					uname = st.nextToken();
	        					out.println("300 Username accepted");
	        					state = 1;
	        				} else if (command.equalsIgnoreCase("QUIT")){
	        					state = 3;
	        				}else if(commandList.contains(command)){
	        					out.println("500 Command not permitted in this state");
	        				} else{
	        					out.println("500 Unknown command");
	        				}
	        				break;
	        			case 1:
	        				if(command.equalsIgnoreCase("PASS")){
	        					String password = st.nextToken();
	        					if(validate(uname, password)){
	        						out.println("200 Access permitted");
	        						state = 2;
	        					}else{
	        						out.println("400 Access denied");
	        						state = 0;
	        					}
	        				} else if (command.equalsIgnoreCase("QUIT")){
	        					state = 3;
	        				} else if(commandList.contains(command)){
	        					out.println("500 Command not permitted in this state");
	        				} else{
	        					out.println("500 Unknown command");
	        				}
	        				break;
	        			case 2:
	        				if(command.equalsIgnoreCase("LISTFILES")){
	        		        	String path=st.nextToken();
	        					String[] filelist = getFileList(path);
	        					for(int i=0;i<filelist.length;i++){
	        						out.println("- " + filelist[i]);
	        					}
	        					out.println("201 End of list");
	        				} else if (command.equalsIgnoreCase("GETINFO")){
	        					String filename = st.nextToken();
	        					File f = new File (filename);
	        					if (f.exists())
	        						out.println("202 OK " + f.length() + " " + new java.util.Date(f.lastModified()));
	        					else
	        						out.println("402 Unknown file");	        						
	        				} else if (command.equalsIgnoreCase("QUIT")){
	        					state = 3;
	        				}else if(commandList.contains(command)){
	        					out.println("500 Command not permitted in this state");
	        				} else{
	        					out.println("500 Unknown command");
	        				}
	        				break;
	        		}
	            }	            
	        	out.println("203 Bye");
	            // Release resources
	        	in.close();
	        	out.close();	        	
	            socketToClient.close();
	        }catch(Exception e){
        		logger.severe("main: " + e);
	            e.printStackTrace();
	        }
    }
        
        public boolean validate(String user, String pass){
        	// Empty function, normally user/pass should be validated against a database/server/system
        	return true;
        }
        
        public String[] getFileList(String path){
        	File f = new File(path);
        	return f.list();
        }
}
