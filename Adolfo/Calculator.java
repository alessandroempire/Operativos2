// este ejemplo fue realizado con fines ilustrativos
// no se hace enfasis en todas las verificaciones que
// que una aplicacion deberia tener.

// Fue conpilado y corrido usando jdk-1.2.2

public interface Calculator 
    extends java.rmi.Remote {
    public final int pepe = 2;

    public long add(long a, long b)
	throws java.rmi.RemoteException;
    
    public long sub(long a, long b)
	throws java.rmi.RemoteException;
    
    public long mul(long a, long b)
	throws java.rmi.RemoteException;
    
    public long div(long a, long b)
	throws java.rmi.RemoteException;
}
