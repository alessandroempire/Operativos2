import java.rmi.*;
import java.net.MalformedURLException;

// este ejemplo fue realizado con fines ilustrativos
// no se hace enfasis en todas las verificaciones que
// que una aplicacion deberia tener.

// Fue conpilado y corrido usando jdk-1.2.2

public class CalculatorClient {
    public static void main(String[] args) {
	String host = null;;
	int port =0;

	if (!((0 < args.length) && (args.length < 3))) {
	    System.err.print("Parametros incorrectos: ");
	    System.err.println("CalculatorClient <hostName> <port>");
	    System.exit(1);
	}

	try {
	    host = args[0];
	    port = Integer.parseInt(args[1]);

	    // Busca al objeto que ofrece el servicio con nombre 
	    // CalculatorService en el Registry que se encuentra en
	    // el host <host> y puerto <port>

	    Calculator c = (Calculator) 
		Naming.lookup("rmi://" + host + ":" + port+ "/CalculatorService");
	    System.out.println( c.sub(4, 3) );
	    System.out.println( c.add(4, 5) );
	    System.out.println( c.mul(3, 6) );
	    System.out.println( c.div(9, 3) );
	}
	catch (MalformedURLException murle) {
	    System.out.println();
	    System.out.println(
			       "MalformedURLException");
	    System.out.println(murle);
	}
	catch (RemoteException re) {
	    System.out.println();
	    System.out.println(
			       "RemoteException");
	    System.out.println(re);
	}
	catch (NotBoundException nbe) {
	    System.out.println();
	    System.out.println(
			       "NotBoundException");
	    System.out.println(nbe);
	}
	catch (java.lang.ArithmeticException ae) {
	    System.out.println();
	    System.out.println(
			       "java.lang.ArithmeticException");
	    System.out.println(ae);
	}
	catch (Exception e) {
	    System.out.println();
	    System.out.println("java.lang.Exception");
	    System.out.println(e);
	}
    }
}
