import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor1 {

	public static class listener extends Thread {
	
	public listener(){
	start();
	}
	
	
	public void run (){
	try {

		DatagramSocket socket;
	      //Keep a socket open to listen to all the UDP trafic that is destined for this port
	      socket = new DatagramSocket(9159, InetAddress.getByName("0.0.0.0"));
	      socket.setBroadcast(true);

	      while (true) {
	        //Receive a packet
	        byte[] recvBuf = new byte[1];
			DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
	        socket.receive(packet); 

	        //Packet received
	        System.out.println(getClass().getName() + ">>>Discovery packet received from: " + packet.getAddress().getHostAddress());
	        //See if the packet holds the right command (message)
	        String message = new String(packet.getData()).trim();
	        byte[] sendData = "1".getBytes();
	          //Send a response
	          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
	          socket.send(sendPacket);
			System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress());
	        
	      }
	    } catch (IOException ex) {
	      System.out.println("Trouble: " + ex);
	    }
	}
	}
		
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

		listener L1 = new listener();
	
    
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

			// Registra con el nombre CalculatorService al objeto c 
			// en el Registry que se encuentra el el host <localhost>
			// y puerto <port>

			Naming.rebind("rmi://localhost:"+"9158"+"/prueba", s);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
    }
}