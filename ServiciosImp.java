import java.util.*;
import java.rmi.server.RemoteServer;

public  class ServiciosImp 
    extends java.rmi.server.UnicastRemoteObject
	implements Servicios {

	public Vector<String> IPS = new Vector<String>();

	public ServiciosImp()
	throws java.rmi.RemoteException {
	super();
    }

    public void registro(String IP)
	throws java.rmi.RemoteException{
		try {
		IPS.add(RemoteServer.getClientHost());
		System.out.println("SE ha registrado el siguiente worker: "+RemoteServer.getClientHost()+"\n");
		}
		catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
		
	};
	
	
	public Vector<String> paso_de_vector()
	throws java.rmi.RemoteException{
		return IPS;
	};
	
	
    
}