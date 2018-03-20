import java.rmi.*;
import java.rmi.registry.*;

public class Assn6Client{
	public static void main(String[] args){
		MethodInterface method;
		try{
			Registry registry = LocateRegistry.getRegistry(1100);
			method = (MethodInterface) registry.lookup(args[0]);
			if(args[1].equals("fibonacci")){
				System.out.print("The fibonacci of " + args[2] + " is: ");
				System.out.println(method.fibonacci(Integer.parseInt(args[2])));
			}
			else if(args[1].equals("factorial")){
				System.out.print("The factorial of " + args[2] + " is: ");
				System.out.print(method.factorial(Integer.parseInt(args[2])));			
			}
		}
		catch(ArrayIndexOutOfBoundsException ex){
			System.out.println("The arguments should be factorial n or fibonacci n");
		}
		catch(Exception ex){
			System.out.println("Exception: " + ex);
		}
	}
}