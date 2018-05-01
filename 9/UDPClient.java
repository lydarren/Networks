import java.net.*;
import java.io.*;

public class UDPClient{
	public static void main(String args[]){
		
		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket();
			byte[] m = args[0].getBytes();
			InetAddress aHost = InetAddress.getByName(args[1]);
			int serverPort = 32710;
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()).trim());
			
		}
		catch(SocketException ex){
			System.out.println("Socket: " + ex.getMessage());
		}
		catch(IOException ex){
			System.out.println("IO: " + ex.getMessage());
		}
		finally{
			if(aSocket != null){
				aSocket.close();
			}
		}
	}
	
	
}