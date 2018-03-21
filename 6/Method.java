import java.rmi.*;
import java.rmi.server.*;

public class Method extends UnicastRemoteObject implements MethodInterface{
	
	public Method() throws RemoteException{
	}
	public int fibonacci(int n) throws RemoteException{
		int prev = 0;
		int current = 1;
		for(int i = 0; i < n; i++){
			int temp = current;
			current += prev;
			prev = temp;
		}
		return current;
	}
	
	public int factorial(int n) throws RemoteException{
		int factorial = n;
		for(int i = n - 1; i > 0; i--){
			factorial *= i;
		}
		return factorial;
	}
}