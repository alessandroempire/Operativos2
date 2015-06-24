// Interfaz de los metodos remotos de un servidor
import java.util.*;

public interface Servicios 
    extends java.rmi.Remote {

    public void registro(String IP)
	throws java.rmi.RemoteException;
	
	 public Vector<String> paso_de_vector()
	throws java.rmi.RemoteException;
    
}
