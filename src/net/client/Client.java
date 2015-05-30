package net.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client {
	private JFrame _frame;
	private JTextArea _messageText;
	private JScrollPane _scrollPane;
	private JButton _pingButton;

	private PrintWriter _out;
	private BufferedReader _in;
	private Socket _socket;
	protected long _pingSendMS;

    public Client() {
    	_frame = new JFrame("client pinger");
    	_messageText = new JTextArea(30, 30); 
    	_messageText.setEditable(false);
    	_scrollPane = new JScrollPane(_messageText);
    	_frame.getContentPane().add(_scrollPane, "Center");
    	
    	_pingButton = new JButton("ping server");
    	_frame.getContentPane().add(_pingButton, "South");
    	
    	_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	_frame.setVisible(true);
    	_frame.pack();
    	
    	hookApplicationOnExitEvent();
        addListenerToButton(_pingButton);
    }

    public void initConnection() throws IOException {
        String serverAddress = JOptionPane.showInputDialog(_frame, "Enter ip or leave it as deafult", "asd", JOptionPane.QUESTION_MESSAGE);
        if (serverAddress == null) {
        	serverAddress = "localhost";
        }
        
        _socket = new Socket(serverAddress, 6776);
        _out = new PrintWriter(_socket.getOutputStream(), true);
        _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

        _messageText.append(_in.readLine() + "\n");
    }
    
    private void hookApplicationOnExitEvent() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    		
    	    @Override
    	    public void run() {
    	    	if (_socket != null) {
    	    		try {
    	    			_socket.close();
    	    		} catch (IOException e) {
    	    			e.printStackTrace();
    	    		}
    	    	}
    	    }
    	});
    }
    
    private void addListenerToButton(JButton button) {
    	button.addActionListener(new ActionListener() {
    		
    		public void actionPerformed(ActionEvent e) {
            	_pingSendMS = System.currentTimeMillis();
    			_out.println("ping");
            	String response = "";
                try {
                    response = _in.readLine();
                    if (response == null || response.equals("")) {
                    	System.exit(0);
                    }
                } catch (IOException ex) {
                	ex.printStackTrace();
                }
                
                _messageText.append(response + "- came in " + (_pingSendMS - System.currentTimeMillis()) + "ms \n");
            	_scrollPane.getVerticalScrollBar().setValue(_messageText.getHeight());
            }
        });
    }
}
