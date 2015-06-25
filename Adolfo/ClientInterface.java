//aca va la interfaz con la lista de las funciones remotas invocables por el Client (este archivo solo lo tendran el Client y el Server)

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote 
{      
	public String solicitarServicio_1(String argumento1 , String argumento2) throws    RemoteException;

	//resto de las funciones a implementar en ClientImplementation.java que seran invocadas desde el Client remotamente
}