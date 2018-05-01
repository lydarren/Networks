import java.net.*;
import java.io.*;
import java.util.*;

public class SERVER_PROGRAM{
	private static HashMap<String, String> table = new HashMap<>();
	private static HashMap<String, String> leafSet = new HashMap<>();
	private static final String myId = "0111";

	public static void main(String args[]){
		createTable();
		createLeafSet();
		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket(32710);
			while(true){
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String guid = new String(request.getData());
				String rep = checkTable(guid);
				request.setData(rep.getBytes());
				DatagramPacket reply = new DatagramPacket(request.getData(),
					request.getLength(), request.getAddress(), request.getPort());
				aSocket.send(reply);
			}
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
		
	
	private static String checkTable(String guid){
		//remove white spaces
		guid = guid.trim();
		char[] id = guid.toCharArray();


		if(guid.length() > 4){
			return "INVALID REQUEST";
		}
		//checks for valid input
		for(char c : id){
			if(c >= '4' || c < '0'){
				return "INVALID REQUEST";
			}
		}
		if(guid.equals(myId)){
			return "0111:52.14.108.231";
		}

		String ret = "";
		String next = "";
		//in leaf set
		if(leafSet.containsKey(guid)){
			return guid + leafSet.get(guid);
		}
		else{
			for(char c : id){
				next += c;
				if(table.containsKey(next)){
					ret += c;
				}
				else{
					return ret + table.get(ret);
				}
			}
		}
		if(table.containsKey(ret)){
			return ret + table.get(ret);
		}
		
		return "NULL";
		
	}

	private static void createTable(){
		
		table.put("0", "111:13.57.206.74");
		table.put("1", "111:52.14.108.231");
		table.put("2", "322:54.177.170.153");
		table.put("3", "333:18.219.42.194");
		
		table.put("00", "11:52.8.111.209");
		table.put("01", "11:13.57.206.74");
		table.put("02", "11:34.209.81.81");
		table.put("03", "32:18.216.240.93");
		
		table.put("010", "3:54.172.160.26");
		table.put("011", "1:13.57.206.74");
		table.put("012", "2:13.57.56.139");
		table.put("013", "3:54.244.201.48");
		
		table.put("0110", ":13.56.78.197");
		table.put("0111", ":13.57.206.74");
		table.put("0112", "NULL");
		table.put("0113", "NULL");
	}
	
	private static void createLeafSet(){
		
		table.put("0103", ":54.172.160.26");
		table.put("0110", ":13.56.78.197");
		table.put("0122", ":13.57.56.139");
		table.put("0133", ":54.244.201.48");
	}
	
	
}
