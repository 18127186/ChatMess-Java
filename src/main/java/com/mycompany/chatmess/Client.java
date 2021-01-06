



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatmess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 *
 * @author trand
 */
public class Client {
    public static ArrayList<ClientConnection> clientarray = null;
    private ClientServer server;
    private InetAddress IPserver;
    private int portServer = 8080;
    private String nameUser = "";
    private boolean isStop = false;
    private static int portClient = 10000; 
    private int timeOut = 1000;  //time to each request is 10 seconds.
    private Socket socketClient;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;
    Client(String ip, int portClient, String user, String msg) throws Exception{
        IPserver = InetAddress.getByName(ip);
	nameUser = user;
	this.portClient = portClient;
	clientarray = MessageTags.Decode.getAllUser(msg);
	new Thread().start();
	server = new ClientServer(nameUser);
        (new Request()).start();
    }
    public void request() throws Exception {
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = MessageTags.Encode.sendRequest(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		msg = (String) serverInputStream.readObject();
		serverInputStream.close();
		clientarray = MessageTags.Decode.getAllUser(msg);
		new Thread().start();
	}
    public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					Thread.sleep(timeOut);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
    public static int getPort() {
		return portClient;
    }
    public void exit() throws IOException{
        isStop = true;
	socketClient = new Socket();
	SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
	socketClient.connect(addressServer);
	String msg = MessageTags.Encode.exit(nameUser);
	serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
	serverOutputStream.writeObject(msg);
	serverOutputStream.flush();
	serverOutputStream.close();
	server.exit();
    }

    void intialNewChat(String IP, int host, String usernameClick) throws Exception {
        final Socket client = new Socket(InetAddress.getByName(IP), host);
	ObjectOutputStream sendrequestChat = new ObjectOutputStream(client.getOutputStream());
	sendrequestChat.writeObject(MessageTags.Encode.sendRequestChat(nameUser));
	sendrequestChat.flush();
	ObjectInputStream receivedChat = new ObjectInputStream(client.getInputStream());
	String msg = (String) receivedChat.readObject();
	if (msg.equals(MessageTags.CHAT_DENY)) {
		OnlineBuddy.Request("Your friend denied connect with you!", false);
		client.close();
		return;
	}
                else new BoxChat(nameUser, usernameClick, client);
    }

}
