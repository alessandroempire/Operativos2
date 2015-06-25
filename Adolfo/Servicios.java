// Interfaz de los metodos remotos de un servidor
import java.util.*;

public interface Servicios 
    extends java.rmi.Remote {

    public String registro()
	throws java.rmi.RemoteException;
	
	 public Vector<String> paso_de_vector()
	throws java.rmi.RemoteException;
	
	public boolean estado()
	throws java.rmi.RemoteException;
	
	public void estado_vector(Vector<String> Nodos)
	throws java.rmi.RemoteException;
	
	public boolean eres_sched()
	throws java.rmi.RemoteException;
	
	public void convertir_sched()
	throws java.rmi.RemoteException;
	    
}
