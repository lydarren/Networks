import java.net.*;
import java.io.*;
import java.lang.*;

public class Assn5Server{
	public static void main(String args[]){
		try{
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);
			while(true){
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		}
		catch(IOException ex){
			System.out.println("Listen:" + ex.getMessage());
		}
	}
	
	static class Connection extends Thread{
		DataInputStream in;
		DataOutputStream out;
		Socket clientSocket;
		public Connection(Socket aClientSocket){
			try{
				clientSocket = aClientSocket;
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
				this.start();
			}
			catch(IOException ex){
				System.out.println("Connection: " + ex.getMessage());
			}
		}
		public void run(){
			try{
				String data = in.readUTF();
				out.writeUTF(new StringBuffer(data).reverse().toString());
			}
			catch(EOFException ex){
				System.out.println("EOF: " + ex.getMessage());
			}
			catch(IOException ex){
				System.out.println("IO:" + ex.getMessage());
			}
			finally{
				try{
					clientSocket.close();
				}
				catch(IOException ex){
					
				}
			}
		}
	}
	
}