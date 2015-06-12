//aca va la implementacion del Worker (este archivo solo lo tendran el Worker y el Server)


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerInterface extends Remote 
{      
	public String despacharServicio_1(String argumento1 , String argumento2) throws    RemoteException;


	//resto de las funciones a implementar en WorkerImplementation.java que seran invocadas desde el Worker remotamente
}