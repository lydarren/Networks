import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Assn6Server{
	
	public static void main(String[] args){
		int port = 1100;
		try{
			String ip = "rmi://" + args[0] + "/cecs327";
			System.setProperty("java.rmi.server.hostname", args[0]);
			Method method = new Method();
			Registry reg = LocateRegistry.createRegistry(port);
			reg.rebind(ip, method);
			
			System.out.println("Assn6Server is ready!\n");
			System.out.println("Use " + ip);
		}
		catch(Exception ex){
			System.out.println("Server Failed: " + ex);
		}
	}
	
}