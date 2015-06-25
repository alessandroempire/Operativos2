//acava la implementacion del servidor (Server)
//java Server -p 20771

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Server{

		public static void main(String argv[]) 
	{
		
		if(argv.length < 2) 
		{
			System.out.println("Uso: java Server -p puertoLocal");
			System.exit(0);
		}
		String puertoLocal="";
		int indexArgs = 0;
		while (indexArgs<argv.length)
		{
			if(argv[indexArgs].equals("-p"))
			{
				indexArgs++;
				puertoLocal=argv[indexArgs];
			}
			else
			{
				System.out.println("ERROR: java s_rmifs -p puertoLocal ");
				System.exit(0);
			}
		
			indexArgs++;
		}
		
		
		//validacion de parametros de entrada
		try 
		{
			LocateRegistry.createRegistry(Integer.parseInt(puertoLocal));
			ClientInterface ci = new ClientImplementation("ScheduleServer");
			Naming.rebind("//127.0.0.1:"+puertoLocal+"/ScheduleServer", ci);
		
		} 
		catch(Exception e) 
		{
			
			System.out.println("FileServer: "+e.getMessage());
			
			e.printStackTrace();
		
		}
	
	} 

}