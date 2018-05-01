import java.net.*;
import java.io.*;

import java.util.*;

public class PastryClient{
	static final int serverPort = 32710;
	static final String ip = "13.57.206.74";
	
	static Random rand = new Random();
	
	public static void main(String args[]){
	
		int[] hops = new int[10];
		
		for(int i = 0; i < 1000; i++){
			try{
				String guid = generateId();
				int h = findHopsToId(guid, ip);
				if(h >= 0){
					//index indicates number of hops
					hops[h] += 1;
				}
				else{
					i--;
				}
				
			}
			catch(SocketTimeoutException ex){
				System.out.println("Socket timeout");
				i--;
			}
			catch(SocketException ex){
				System.out.println("Socket: " + ex.getMessage());
				i--;
			}
			catch(IOException ex){
				System.out.println("IO: " + ex.getMessage());
				i--;
			}
		}
		for(int i = 0; i < 10; i++){
			System.out.println(hops[i]);
		}
	}
	public static int findHopsToId(String guid, String start) throws SocketTimeoutException, SocketException, IOException{
		
		DatagramSocket aSocket = null;
		int hops = 0;
		HashSet<String> visited = new HashSet<String>();
		try{
			String[] info = {guid, start};
			byte[] m = guid.getBytes();
			//System.out.println("Looking for id: " + guid + ":" + info[1]);
			aSocket = new DatagramSocket();
			hops = 0;
			do {
				byte[] buffer = new byte[1000];
				InetAddress aHost = InetAddress.getByName(info[1]);
				DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
				aSocket.send(request);
				//timeout exception, will be caught if routing server is unreachable
				aSocket.setSoTimeout(10000);
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);

				System.out.println("Reply: " + new String(reply.getData()).trim());
				String abc = new String(reply.getData()).trim();
				info = abc.split(":");
				//System.out.println("Searching: " + abc);
				hops++;
				if((info[0].toLowerCase()).equals("null")){
					//this check is if the destination server is null or unreachable
					//System.out.println("Null received!");
					break;
				}
				if(info.length >= 2){
					//this check is if the destination server is null or unreachable
					if((info[1].toLowerCase()).equals("null")){
						//System.out.println("Null received!");
						break;
					}				
					//unable to reach, will keep visiting visited nodes
					if(visited.contains(info[1])){
						//System.out.println("Same IP received and checked");
						hops = -1;
						break;
					}
				}
				if(info.length < 2){
					//format error from request
					//System.out.println("Incorrect format received");
					hops = -1;
					break;
				}
				visited.add(info[1]);
			} while(info[0].equals(guid) == false);
		}
		finally{
			if(aSocket != null){
				aSocket.close();
			}
		}
		
		return hops;
	}
	
	public static String generateId(){
		//Random rand = new Random();
		String id = "";
		for(int i = 0; i < 4; i++){
			int r = rand.nextInt(4);
			id += Integer.toString(r);
		}
		return id;
		
	}
	
}