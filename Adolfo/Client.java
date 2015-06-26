//aca va la implementacion del Client
//java Client -h 127.0.0.1 -p 20771

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.rmi.*;
import java.util.Enumeration;

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

		String name = "rmi://" + serverDir + ":"+serverPort+ "/ScheduleServer";
		ClientInterface ci = (ClientInterface) Naming.lookup(name);

		System.out.print(ci.solicitarServicio_1("get_time","get_time"));

		System.exit(0);

	}
        
        
	public String descubrir_schd() {
		
		// Find the server using UDP broadcast
	int sec = 0;
	String IP = "";
	while (sec<=10) {
		try {
			//Open a random port to send the package
			DatagramSocket c = new DatagramSocket();
			c.setBroadcast(true);

			byte[] sendData = "1".getBytes();

			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback interface
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}
					// Send the broadcast package!
					try {
						System.out.println("enviando " );
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 9159);
						c.send(sendPacket);
					} catch (Exception e) {
					}				
				}
			}
			//Wait for a response
			byte[] recvBuf = new byte[1];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			try {
				c.setSoTimeout(1000);
				c.receive(receivePacket);
				
				//We have a response
				System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
				IP = receivePacket.getAddress().getHostAddress();
				//Close the port!
				c.close();
				return IP;
			} 		catch (Exception e) {
					}				
			} catch (IOException ex) {
				System.out.println("Trouble: " + ex);
			}
			try {
			 Thread.sleep(1000);
			 sec++;
			}catch (Exception e) {
				System.out.println("Problema: " + e );
			}
	}
	return null;
	}

}