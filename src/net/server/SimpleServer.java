package net.server;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleServer {

	public static void main(String[] args) {
		System.out.println("Starting server.");
        int clientNumber = 0;
        ServerSocket listener = null;
        
		try {
			listener = new ServerSocket(6776);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
        try {
            while (true) {
                new SimpleServerThread(listener.accept(), clientNumber++).start();
            }
        } 
        catch (IOException e) {
			e.printStackTrace();
		}
        finally {
            try {
				listener.close();
			} 
            catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

}
