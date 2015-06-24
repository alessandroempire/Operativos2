import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	public static Servicios s;
	public static Vector<String> vector_sched = new Vector<String>();
	public static String my_IP = null;
	public static String sched_IP = null;
	public static Vector<String> nodos = new Vector<String>();
	public static boolean soy_sched = false;
	public static listener L1;
	public static comprobador_sched daemon;
	public static comprobador_sched daemon_nodo;
	public static Servicios scheduler;
	
	
	public Servidor() {
		super();
	}

	public static class comprobador_sched extends Thread {
			
	public comprobador_sched(){
	start();
	}
		
	public void run (){
		while(true){
			try {
				s.estado_vector(((ServiciosImp)s).IPS);
				Thread.sleep(20000);					
			}
			catch (Exception e) {
					System.out.println("Problema: " + e );
			}	
		}
	}
	}
	
	public static class comprobador_nodo extends Thread {
	public comprobador_nodo(){
	start();
	}
		
	public void run (){
		while(true){
			try {
				s.estado_vector(vector_sched);
				if (vector_sched.isEmpty()){
				// Se perdio la conexion con el scheduler
					Iterator<String> iterador = nodos.iterator();
					while (iterador.hasNext()) {
						String IP = iterador.next();
						if IP.equals(my_IP) {
							s.convertir_sched();
							if (L1.isAlive()){
								L1.destroy();
							}	
							L1 = new listener();
							iterador.remove();
							nodos.remove(IP);
							if (daemon.isAlive()){
								daemon.destroy();
							}							
							daemon = new comprobador_sched();
													
							Thread.destroy();
							}							
						try {
							Servicios nodo = (Servicios) 
							Naming.lookup("rmi://" + IP + ":" + "9158"+ "/prueba");
							if (!nodo.estado()) {
							System.out.println("Se detecto un error en "+IP+" sera sacado de los nodos registrados");
							iterador.remove();
							Nodos.remove(IP);
							}
							else{
							my_IP=nodo.registro();
							sched_IP = IP;
							vector_sched = new Vector<String>();
							vector_sched.add(IP);
							break;
							}
						}
						catch (Exception e) {
							System.out.println("Problema: " + e + " " + IP+" Sera sacado");
							iterador.remove();
							Nodos.remove(IP);
							continue;
						}		
					}
					
				}
				nodos = (Vector)scheduler.paso_de_vector().clone();
				Thread.sleep(20000);					
			}
			catch (Exception e) {
					System.out.println("Problema: " + e );
			}	
		}
	}
	
	}

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
	c.setSoTimeout(10000);
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
	catch (SocketTimeoutException ex) {
	System.out.println("No hay schedulers activos, sere uno: " + ex);
		L1 = new listener();
		daemon = new comprobador_sched();
	}	
	return IP;
	
	}

   
    public static void main(String[] args) {

	
		s = null;
		Servidor1 S1 = new Servidor1();
    
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
			
		s = new ServiciosImp(true);
    
			// Registra con el nombre CalculatorService al objeto c 
			// en el Registry que se encuentra el el host <localhost>
			// y puerto <port>

			Naming.rebind("rmi://localhost:"+"9158"+"/prueba", s);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
		
		sched_IP = S1.descubrir_schd();
		if (sched_IP != null) {
		Servicios scheduler = (Servicios) 
		Naming.lookup("rmi://" + sched_IP + ":" + "9158"+ "/prueba");
	    my_IP=scheduler.registro();
		}
		
    }
}