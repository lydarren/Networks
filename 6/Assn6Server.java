import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class Assn6Server{
	
	public static void main(String[] args){
		try{
			String ip = "rmi://" + args[0] + "/cecs327";
			System.setProperty("java.rmi.server.hostname", args[0]);
			MethodInterface method = new Method();
			
			Naming.rebind("rmi://127.0.0.1/cecs327", method);
			
			System.out.println("Assn6Server is ready!\n");
			System.out.println("Use " + ip);
		}
		catch(Exception ex){
			System.out.println("Server Failed: " + ex);
		}
	}
	
}