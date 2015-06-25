import java.util.*;
import java.rmi.server.RemoteServer;
import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import java.net.*;

public  class ServiciosImp 
    extends java.rmi.server.UnicastRemoteObject
	implements Servicios {

	public Vector<String> IPS = new Vector<String>();
	public boolean soy_sched = false;

	public ServiciosImp(boolean sched)
	throws java.rmi.RemoteException {
// 	super();
	soy_sched = sched;
    }

    public String registro()
	throws java.rmi.RemoteException{
		try {
// 		if (! InetAddress.getLocalHost().getHostAddress().equals(RemoteServer.getClientHost())){
			IPS.add(RemoteServer.getClientHost());
			System.out.println("SE ha registrado el siguiente worker: "+RemoteServer.getClientHost()+"\n");
			return RemoteServer.getClientHost();
// 		}
		}
		catch (Exception e) {
			System.out.println("Trouble: " + e);
		}		
		return " ";
	};
	
	
	public Vector<String> paso_de_vector()
	throws java.rmi.RemoteException{
		return IPS;
	};
	
	public boolean estado()
	throws java.rmi.RemoteException{
	return true;
	// Posteriormente se puede agregar alguna funcion que retermine que algo esta 
	// fallando	
	};
	
	public void estado_vector(Vector<String> Nodos)
	throws java.rmi.RemoteException{
	
		Iterator<String> iterador = Nodos.iterator();
		while (iterador.hasNext()) {
			String IP = iterador.next();
			try {
				Servicios nodo = (Servicios) 
				Naming.lookup("rmi://" + IP + ":" + "9158"+ "/prueba");
				if (!nodo.estado()) {
				System.out.println("Se detecto un error en "+IP+" sera sacado de los nodos registrados");
				iterador.remove();
				Nodos.remove(IP);
				}
			}
			catch (Exception e) {
				System.out.println("Problema: " + e + " " + IP+" Sera sacado");
				iterador.remove();
				Nodos.remove(IP);
				continue;
			}		
		}
		return;
	};
	
	public boolean eres_sched()
	throws java.rmi.RemoteException{
	return soy_sched;
	}
	
	public void convertir_sched()
	throws java.rmi.RemoteException{
	soy_sched = true;
	};
    
}