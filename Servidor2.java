import java.rmi.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Servidor2 {

	public static Servicios s;
	public static Vector<String> vector_sched = new Vector<String>();
	public static String my_IP = null;
	public static String sched_IP = null;
	public static Vector<String> nodos = new Vector<String>();


	public Servidor2() {
		super();
	};

	
	public static class comprobador_nodo extends Thread {
	public comprobador_nodo(){
	start();
	}
		
	public void run (){
		while(true){
			try {
				System.out.println("comprobando al sched");
				s.estado_vector(vector_sched);
				Thread.sleep(20000);					
			}
			catch (Exception e) {
					System.out.println("Problema: " + e );
			}	
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
			s = new ServiciosImp(false);

// 			Registra con el nombre CalculatorService al objeto c 
// 			en el Registry que se encuentra el el host <localhost>
// 			y puerto <port>

			Naming.rebind("rmi://localhost:"+"9158"+"/prueba", s);
		
		System.out.println("Probando");
		sched_IP = S2.descubrir_schd();
		System.out.println(sched_IP);
		vector_sched.add(sched_IP);
		Servicios scheduler = (Servicios) 
		Naming.lookup("rmi://" + sched_IP + ":" + "9158"+ "/prueba");
	    my_IP=scheduler.registro();
	    
	    System.out.println("Listo");
	    System.out.println("viendo el vector");
	    System.out.println(scheduler.paso_de_vector());
	    nodos = (Vector)scheduler.paso_de_vector().clone();
	    comprobador_nodo daemon_nodo = new comprobador_nodo();
	   
	    }
	     catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
    }
}