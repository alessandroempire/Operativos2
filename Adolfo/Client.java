//aca va la implementacion del Client
//java Client -h 127.0.0.1 -p 20771

import java.net.MalformedURLException;
import java.rmi.*;

public class Client
{

	public static void main(String argv[]) throws MalformedURLException, RemoteException, NotBoundException 
	{

		String serverDir = "";
		String serverPort = "";

		if(argv.length < 3) 
		{
			System.out.println("Uso: java Client -h ServerHost -p ServerPort");
			System.exit(0);
		}
		int indexArgs = 0;
		while (indexArgs<argv.length)
		{
			
			if(argv[indexArgs].equals("-h"))
			{
				indexArgs++;
				serverDir=argv[indexArgs];
			}
			else if(argv[indexArgs].equals("-p"))
			{
				indexArgs++;
				serverPort=argv[indexArgs];	
			}
			else
			{
				System.out.println("ERROR: Uso: java Client -h ServerHost -p ServerPort");
				System.exit(0);
			}
		
			indexArgs++;
		}

		String name = "//" + serverDir+":"+serverPort+ "/ScheduleServer";
		ClientInterface ci = (ClientInterface) Naming.lookup(name);

		System.out.print(ci.solicitarServicio_1("",""));

		System.exit(0);

	}



}