import java.io.IOException;

import java.net.ServerSocket;

import java.net.Socket;


public class Serveur {


    	public static void main(String[] args) {
		
		InetAddress LocaleAdresse ;
		InetAddress ServeurAdresse;

		try {

			LocaleAdresse = InetAddress.getLocalHost();
			System.out.println("L'adresse locale est : "+LocaleAdresse ); 
		
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
	}

}
