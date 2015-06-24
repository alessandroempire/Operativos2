import java.rmi.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor2 {

	public Servidor2() {
	super();
	};


	public String descubrir_schd() {
		
		// Find the server using UDP broadcast
	String IP = "";
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
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 9159);
			c.send(sendPacket);
		} catch (Exception e) {
		}
		
		}
	}
	//Wait for a response
	byte[] recvBuf = new byte[1];
	DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
	c.receive(receivePacket);

	//We have a response
	System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
	IP = receivePacket.getAddress().getHostAddress();
	//Close the port!
	c.close();
	} catch (IOException ex) {
	System.out.println("Trouble: " + ex);
	}	
	return IP;
	
	}
   
    public static void main(String[] args) {
	
	String IP;
    Servidor2 S2 = new Servidor2();
    
		try {

			// Crea un Registry en el puerto especificado
			LocateRegistry.createRegistry(9158);	    
		}
		catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		}
		catch (Exception e) {
			System.out.println();
			System.out.println("java.lang.Exception");
			System.out.println(e);
		}

		try {
			Servicios s = new ServiciosImp();

// 			Registra con el nombre CalculatorService al objeto c 
// 			en el Registry que se encuentra el el host <localhost>
// 			y puerto <port>

			Naming.rebind("rmi://localhost:"+"9158"+"/prueba", s);
		
		System.out.println("Probando");
		IP = S2.descubrir_schd();
		Servicios scheduler = (Servicios) 
		Naming.lookup("rmi://" + IP + ":" + "9158"+ "/prueba");
	    scheduler.registro(InetAddress.getLocalHost().getHostAddress());
	    System.out.println("Listo");
	    System.out.println("viendo el vector");
	    System.out.println(scheduler.paso_de_vector());
	   
	    }
	     catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
    }
}