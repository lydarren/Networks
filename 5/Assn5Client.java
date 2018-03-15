import java.net.*;
import java.io.*;

public class Assn5Client{
	public static void main(String args[]){
		
		Socket s = null;
		try{
			int serverPort = 7896;
			s = new Socket(args[0], serverPort);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(args[1]);
			String data = in.readUTF();
			System.out.println("Reply from the server: " + data);
		}
		catch(UnknownHostException ex){
			System.out.println("Sock:" + ex.getMessage());
		}
		catch(EOFException ex){
			System.out.println("EOF: " + ex.getMessage());
		}
		catch(IOException ex){
			System.out.println("Listen:" + ex.getMessage());
		}
		finally{
			if(s != null){
				try{
					s.close();
				}
				catch(IOException ex){
					
				}
			}
		}
	}
	
	
	
}