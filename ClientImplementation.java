//aca va la implementacion de las funciones locales invocables por el Cliente

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplementation extends UnicastRemoteObject    implements ClientInterface
{

	private String nombre;
	public ClientImplementation(String s) throws RemoteException 
	{
		super();
		nombre = s;
	}



	public String solicitarServicio_1(String argumento1 , String argumento2){
		return "";
	}



}