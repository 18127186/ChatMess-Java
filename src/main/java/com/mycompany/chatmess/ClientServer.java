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
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author trand
 */
public class ClientServer {
    private String username = "";
    private ServerSocket serverClient;   
    private int port;
    public String ip;
    public static String ipofServer = GUI.ipofServer;
    public String messageToRequestUpdateClienListString;
    private boolean isStop = false;
    public ClientServer(String name) throws Exception {
		username = name;
		port = Client.getPort();
		serverClient = new ServerSocket(port);
                Thread thread = new Thread(new WaitPeerConnect());
		thread.start();
	}
	
    public void exit() throws IOException {
		isStop = true;
		serverClient.close();
	}

    class WaitPeerConnect implements Runnable {

		Socket connection;
		ObjectInputStream getRequest;

		@Override
		public void run() {
			while (!isStop) {
				try {
					connection = serverClient.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					String name = MessageTags.Decode.getNameRequestChat(msg);
					int res = OnlineBuddy.Request("Account: " + name + " want to connect with you !", true);
					ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
					if (res == 1) {
						send.writeObject(MessageTags.CHAT_DENY);
					} else if (res == 0) {
                                            OnlineBuddy ob = new OnlineBuddy();
                                            //ip = Inet4Address.getLocalHost().getHostAddress(); // dia chi ip cua mang
                                            ip = ipofServer; //ip cua server
                                            InetAddress ipServer = InetAddress.getByName(ip);
                                            int portServer = 1234;
                                            Socket socketClient = new Socket(ipServer, portServer);


                                            String messageToRequestUpdateClienListString = MessageTags.Encode.getCreateAccount(username, "0");
                                            ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
                                            serverOutputStream.writeObject(msg);
                                            serverOutputStream.flush();
                                            ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
                                            msg = (String) serverInputStream.readObject();

                                            socketClient.close();
                                            Client.clientarray = MessageTags.Decode.getAllUser(msg);
                                            send.writeObject(MessageTags.CHAT_ACCEPT);
                                            System.out.println("Access");
                                            new BoxChat(username, name, connection);
					}
					send.flush();
				} catch (Exception e) {
					break;
				}
			}
			try {
				serverClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
