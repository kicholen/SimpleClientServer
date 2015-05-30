package net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleServerThread extends Thread {
	private int _clientNumber;
	private Socket _socket;

    public SimpleServerThread(Socket socket, int clientNumber) {
    	_clientNumber = clientNumber;
    	_socket = socket;
    	System.out.println("New client " + clientNumber + " at " + socket);
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            out.println("You are client: " + _clientNumber + ".");
            
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                out.println(input);
            }
        } 
        catch (IOException e) {
        	System.out.println("Oops somehing went wrong with client: " + _clientNumber + ": " + e);
        } 
        finally {
            try {
            	_socket.close();
            }
            catch (IOException e) {
            	System.out.println(e);
            }
            System.out.println("Connection with client: " + _clientNumber + " closed.");
        }
    }
	
}
