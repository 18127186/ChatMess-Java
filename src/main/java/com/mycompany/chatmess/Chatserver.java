
package com.mycompany.chatmess;
import java.awt.EventQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Chatserver {
    private static ArrayList<ClientConnection> clientlist;
    public static JFrame frame1;
    public static  JButton button;
    final static boolean shouldFill = true;
    public static GridBagConstraints c = new GridBagConstraints();			
    private static ObjectOutputStream obOutputClient;		
    private static ObjectInputStream obInputStream;			
    public static boolean isStop = false, isExit = false;		
    public static ServerSocket serverSocket;
    public static Socket socket;
    public static JTextArea textfornoti;
    public static JScrollPane scrollPane;
    public static boolean serverRun = true;
    public static void FirstOfAddComponents(Container pane){

        pane.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
 
    }
    //create on GridBagConstraints c
    public static void CreateComponents(int gridx,int gridy, int ipady,int ipadx){
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = new Insets(5,5,5,5); 
        c.ipady = ipady; 
        c.ipadx = ipadx; 
    }
    public static void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);       
        
        button = new JButton("Start Server");
        CreateComponents(0, 0, 20, 80);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Chatserver server = new Chatserver(1234);
                    textfornoti.setText( "Server listening ... ");  
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getClass().getName()+": "+ex.getMessage());
                }
            }
        });
        
        button = new JButton("Stop Server");
        CreateComponents(1, 0, 20, 80);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    textfornoti.append("\n Server stop ");
                    stop();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getClass().getName()+": "+ex.getMessage());
                }
            }
        });
        textfornoti = new JTextArea(5,20);
        scrollPane = new JScrollPane(textfornoti);
        CreateComponents(0, 1, 20, 80);
        pane.add(scrollPane,c);
    }
    public static void stop() throws IOException{
        isStop = true;
	serverSocket.close();							
	socket.close();
        System.out.print("Server Stop");
    }
    public static void createAndShowGUI() {
        
        frame1 = new JFrame("Server");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    Logger.getLogger(OnlineBuddy.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });
       
    }
    
    public Chatserver(int port) throws Exception {
	serverSocket = new ServerSocket(port);
        clientlist = new ArrayList<ClientConnection>();
	(new WaitConnect()).start();
        
	}
    public static String sendSessionAccept()throws Exception{
        String msg = MessageTags.SESSION_ACCEPT_OPEN;
	int size = clientlist.size();				
	for (int i = 0; i < size; i++) {		
		ClientConnection client = clientlist.get(i);	
		msg += MessageTags.CLIENT_OPEN;			
		msg += MessageTags.CLIENT_NAME;
		msg += client.getName();
		msg += MessageTags.CLIENT_NAME_CLOSE;
		msg += MessageTags.IP_OPEN;
		msg += client.getHost();
		msg += MessageTags.IP_CLOSE;
		msg += MessageTags.PORT_OPEN;
		msg += client.getPort();
		msg += MessageTags.PORT_CLOSE;
		msg += MessageTags.CLIENT_CLOSE;			
	}
	msg += MessageTags.SESSION_ACCEPT_CLOSE;	
	return msg;
    }
    private static boolean Exit(String name) throws Exception{
        if(clientlist == null){
            return false;
        }
        int sizeList = clientlist.size();
        for(int i=0;i<sizeList;i++){
            ClientConnection client  = clientlist.get(i);
            if(client.getName().equals(name)) return true;
        }
        return false;
    }
    private static void saveClient(String user, String ip, int port) throws Exception{
        ClientConnection client = new ClientConnection();
        if (clientlist.size() == 0)				
            clientlist = new ArrayList<ClientConnection>();
	client.setPeer(user, ip, port);		
	clientlist.add(client);
        textfornoti.append("\n" + client.getName() + " " +client.getPort() + " Connect ");
    }
    private static boolean waitConnect() throws Exception {
        socket = serverSocket.accept();
        obInputStream = new ObjectInputStream(socket.getInputStream());
        String msg = (String) obInputStream.readObject();
        ArrayList<String> getData = MessageTags.Decode.getUser(msg);
        if (getData != null) {
            if (!Exit(getData.get(0))) {						
		saveClient(getData.get(0), socket.getInetAddress().toString(), Integer.parseInt(getData.get(1)));	
            }
        }
        return true;
    }
    private static class WaitConnect extends Thread {
        @Override
        public void run(){
            super.run();
            try {
                while (!isStop) {                    
                    if(waitConnect()){
                        if (isExit) {   
                            isExit = false;
			} else {
                            obOutputClient = new ObjectOutputStream(socket.getOutputStream());
                            obOutputClient.writeObject(sendSessionAccept());
                            obOutputClient.flush();
                            obOutputClient.close();
			}
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
