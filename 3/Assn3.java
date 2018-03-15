
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Assn3{
	
	static private FTPClient ftp = null;
	
	public static void main(String[] args){
		
		String command = null;
		//server IP
		String serverIP = args[0];
		//parse id:password into 2 parts
		String[] idPassword = args[1].split(":");
		//checks if the : format is met		
		if(idPassword.length > 1){
			//log into ftp server
			try{					
				ftp = new FTPClient();
				ftp.connect(serverIP);
				ftp.login(idPassword[0], idPassword[1]);
			}
			catch(IOException e){
				System.err.println("Unable to connect to FTP service");	
			}
		}
	
		for(int i = 2; i < args.length; i++){
			command = parseCommand(args[i]);
			if(command.equals("ls")){
				printFiles(ftp);
			}
			else if(command.equals("cd")){
				String dir = parseDirectoryFile(args[i]);
				changeDirectory(dir);
			}
			else if(command.equals("delete")){
				String file = parseDirectoryFile(args[i]);
				deleteFile(ftp, file);
			}
			else if(command.equals("get")){
				String dirFile = parseDirectoryFile(args[i]);
				getFile(ftp, dirFile);
			}
			else if(command.equals("put")){
				String filename = parseDirectoryFile(args[i]);
				File file = new File(filename);
				if(file.isFile()){
					putFile(ftp, filename, "");
				}
				else if(file.isDirectory()){
					putDirectory(ftp, filename);
				}
			}
			else if(command.equals("mkdir")){
				String dir = parseDirectoryFile(args[i]);
				try{
					ftp.makeDirectory(dir);
				}
				catch(IOException e){
					System.err.println("Unable to make directory: " + dir);
				}
			}
			else if(command.equals("rmdir")){
				String dir = parseDirectoryFile(args[i]);
				removeDirectory(ftp, dir);
			}
			else{
				System.out.println("Wrong command input.");
			}
		}
		
		try{
			ftp.logout();
			ftp.disconnect();
		}
		catch(IOException e){
			System.err.println("Unable to logout or disconnect from ftp");
		}
	}
	
	private static String parseCommand(String str){
		if(str.contains(" ")){	
			return str.substring(0, str.indexOf(" ")); 
		}
		return str;
	}
	
	private static String parseDirectoryFile(String str){
		return str.substring(str.indexOf(" ") + 1, str.length());
	}
	
	private static void removeDirectory(FTPClient ftpc, String folderName){
		try{
			//change to directory to get files to delete
			ftpc.changeWorkingDirectory(folderName);
			//didnt cd into folder so it does not exists
			if(ftpc.getReplyCode() == 550){
				System.err.println("The directory does not exist!");
				return;
			}
			FTPFile[] files = ftpc.listFiles();
			for(FTPFile file : files){
				if(file.isFile()){
					ftpc.deleteFile(file.getName());
				}
				else if(file.isDirectory()){
					//directory is found inside the current dir
					removeDirectory(ftpc, file.getName());
				}
			}
			//go back to remove the empty dir
			ftpc.changeToParentDirectory();
			ftpc.removeDirectory(folderName);
		}
		catch(IOException ex){
			System.err.println("Unable to remove directory: " + folderName);
		}
	}
	
	private static void getDirectory(FTPClient ftpc, String dirName){
		try{
			//create the directory in the local system
			File dir = new File(dirName);
			dir.mkdir();
			ftpc.changeWorkingDirectory(dirName);
			FTPFile[] files = ftpc.listFiles();
			for(FTPFile file : files){
				if(file.isFile()){
					getFile(ftpc, file.getName(), dirName);
				}
				else if(file.isDirectory()){
					//go back to get correct filepath
					ftpc.changeToParentDirectory();
					getDirectory(ftpc, dirName + "/" + file.getName());
				}
			}
			//cd back to original location before function call
			ftpc.changeToParentDirectory();
		}
		catch(IOException ex){
			System.err.println("Unable to get directory: " + dirName);
		}
	}

	private static void getFile(FTPClient ftpc, String fileName, String dirName){
		//create the path for the file to download
		//empty string downloads to current working dir
		String path = "";
		if(!dirName.equals("")){
			path = dirName + "/";
		}
		try(OutputStream ostream = new BufferedOutputStream(new FileOutputStream(path + fileName))){
			ftpc.retrieveFile(fileName, ostream);
		}
		catch(IOException ex){
			System.err.println("Unable to get file: " + fileName);
		}
	}
	
	private static void putDirectory(FTPClient ftpc, String dirName){
		try{		
			File dir = new File(dirName);
			File[] files = dir.listFiles();
			//create the directory to ftp server
			ftpc.makeDirectory(dirName);
			ftpc.changeWorkingDirectory(dirName);
			for(File file : files){
				if(file.isFile()){
					putFile(ftpc, file.getName(), dirName);
				}
				else if(file.isDirectory()){
					//go back to get correct filepath
					ftpc.changeToParentDirectory();
					putDirectory(ftpc, dirName + "/" + file.getName());
				}
			}
			//cd back to original location before function call
			ftpc.changeToParentDirectory();
		}
		catch(IOException ex){
			System.err.println("Unable to put directory: " + dirName);
		}
	}
	
	
	private static void putFile(FTPClient ftpc, String filename, String dirName){
		//create the path for the local location
		//empty string uploads from current working dir
		String path = "";
		if(!dirName.equals("")){
			path = dirName + "/";
		}
		File localFile = new File(path + filename);
		try(InputStream istream = new FileInputStream(localFile)){
			ftpc.storeFile(filename, istream);
		}
		catch(FileNotFoundException ex){
			System.err.println("File Not Found: " + filename);
		}
		catch(IOException ex){
			System.err.println("Unable to import file: " + filename);
		}
	}
	
	
	private static void printFiles(FTPClient ftpc){
		try{
			FTPFile[] files = ftpc.listFiles();
			for(FTPFile f : files){
				System.out.println(f.getName());
			}
		}
		catch(IOException ex){
			System.err.println("Unable to read files");
		}
	}
	
	private static void changeDirectory(String dest){
		if(dest.equals("..")){
			try{
				ftp.changeToParentDirectory();
			}
			catch(IOException ex){
				System.err.println("Can not change to parent directory");
			}
		}
		else{
			try{
				ftp.changeWorkingDirectory(dest);
			}
			catch(IOException e){
				System.err.println("Unable to change directory to: " + dest);
			}
		}
	}
	private static void deleteFile(FTPClient ftpc, String fileName){
		try{
			ftpc.deleteFile(fileName);
		}
		catch(IOException e){
			System.err.println("Unable to delete file: " + fileName);
		}
	}
	private static void getFile(FTPClient ftpc, String fileName){
		try{
			//check if file or directory exists in the current directory
			FTPFile[] listFiles = ftpc.listFiles();
			for(FTPFile file : listFiles){
				if((file.getName()).equals(fileName)){
					if(file.isDirectory()){
						getDirectory(ftpc, fileName);
					}
					else{
						getFile(ftpc, fileName, "");
					}
				}
			}
			System.out.println();
		}
		catch(IOException ex){
			System.err.println("Unable to use get command");
		}
	}
}