/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatmess;

/**
 *
 * @author trand
 */
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class MessageTags {

    public final static String SESSION_OPEN = "<SESSION>";
    public final static String SESSION_CLOSE = "</SESSION>";
    public final static String CLIENT_NAME = "<CLIENT_NAME>";
    public final static String CLIENT_NAME_CLOSE = "</CLIENT_NAME>";
    public final static String PORT_OPEN = "<PORT>";
    public final static String PORT_CLOSE = "</PORT>";
    public final static String SESSION_KEEP_OPEN = "<SESSION_KEEP>";
    public final static String SESSION_KEEP_CLOSE = "</SESSION_KEEP>";
    public final static String STATUS_OPEN = "<STATUS>";
    public final static String STATUS_CLOSE = "</STATUS>";
    public final static String SESSION_DENY = "<SESSION_DENY/>";
    public final static String SESSION_ACCEPT_OPEN = "<SESSION_ACCEPT>";
    public final static String SESSION_ACCEPT_CLOSE = "</SESSION_ACCEPT>";
    public final static String CHAT_REQ_OPEN = "<CHAT_REQ>";
    public final static String CHAT_REQ_CLOSE = "</CHAT_REQ>";
    public final static String IP_OPEN = "<IP>";
    public final static String IP_CLOSE = "</IP>";
    public final static String CHAT_DENY = "<CHAT_DENY/>";
    public final static String CHAT_ACCEPT = "<CHAT_ACCEPT/>";
    public final static String CLIENT_OPEN = "<CLIENT>";
    public final static String CLIENT_CLOSE = "</CLIENT>";
    public final static String CHAT_CLOSE_TAG = "<CHAT_CLOSE />";
	// ------------------
    public final static String SERVER_ONLINE = "RUNNING";
    public final static String SERVER_OFFLINE = "STOP";
    public static JFrame frame;
    public static int show(JFrame frame, String msg, boolean type){
        if (type) 
            return JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.YES_NO_OPTION);
	JOptionPane.showMessageDialog(frame, msg);
	return -1;
    }
    public static class Encode{
        public static Pattern checkMessage = Pattern.compile("[^<>]*[<>]");
        public static String getCreateAccount(String name, String port) {
		return SESSION_OPEN + CLIENT_NAME + name
				+ CLIENT_NAME_CLOSE + PORT_OPEN + port
				+ PORT_CLOSE + SESSION_CLOSE;
	}
        public static String exit(String name) {
		return SESSION_KEEP_OPEN + CLIENT_NAME
				+ name + CLIENT_NAME_CLOSE + STATUS_OPEN
				+ SERVER_OFFLINE + STATUS_CLOSE
				+ SESSION_KEEP_CLOSE;
	}
	public static String sendRequestChat(String name) {
		return CHAT_REQ_OPEN + CLIENT_NAME + name
				+ CLIENT_NAME_CLOSE + CHAT_REQ_CLOSE;
	}
        public static String sendRequest(String name) {
		return SESSION_KEEP_OPEN + CLIENT_NAME
				+ name + CLIENT_NAME_CLOSE + STATUS_OPEN
				+ SERVER_ONLINE + STATUS_CLOSE
				+ SESSION_KEEP_CLOSE;
	}
    }
    public static class Decode{
        public static  Pattern createAccount = Pattern.compile(SESSION_OPEN + CLIENT_NAME + ".*"
					+ CLIENT_NAME_CLOSE + PORT_OPEN + ".*"
					+ PORT_CLOSE + SESSION_CLOSE);
	public static  Pattern users = Pattern.compile(SESSION_ACCEPT_OPEN
			+ "(" + CLIENT_OPEN + CLIENT_NAME + ".+"
			+ CLIENT_NAME_CLOSE + IP_OPEN + ".+"
			+ IP_CLOSE + PORT_OPEN + "[0-9]+"
			+ PORT_CLOSE + CLIENT_CLOSE + ")*"
			+ SESSION_ACCEPT_CLOSE);
	public static  Pattern request = Pattern.compile(SESSION_KEEP_OPEN + CLIENT_NAME
					+ "[^<>]+" + CLIENT_NAME_CLOSE
					+ MessageTags.STATUS_OPEN + "(" + MessageTags.SERVER_ONLINE + "|"
					+ MessageTags.SERVER_OFFLINE + ")" + MessageTags.STATUS_CLOSE
					+ SESSION_KEEP_CLOSE);
        public static ArrayList<String> getUser(String msg){
            ArrayList<String> user = new ArrayList<>();
            if (createAccount.matcher(msg).matches()) {
			Pattern findName = Pattern.compile(CLIENT_NAME + ".*"
					+ CLIENT_NAME_CLOSE);
			Pattern findPort = Pattern.compile(PORT_OPEN + "[0-9]*"
					+ PORT_CLOSE);
			Matcher find = findName.matcher(msg);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(13, name.length() - 14));
				find = findPort.matcher(msg);
				if (find.find()) {
					String port = find.group(0);
					user.add(port.substring(6, port.length() - 7));
				} else
					return null;
			} else
				return null;
		} else
			return null;
            return user;
        }
        public static ArrayList<ClientConnection> getAllUser(String msg){
            ArrayList<ClientConnection> user = new ArrayList<>();
            Pattern findPeer = Pattern.compile(CLIENT_OPEN
                    + CLIENT_NAME + "[^<>]*" + CLIENT_NAME_CLOSE
                    + IP_OPEN + "[^<>]*" + IP_CLOSE
                    + PORT_OPEN + "[0-9]*" + PORT_CLOSE
                    + CLIENT_CLOSE);  
		Pattern findName = Pattern.compile(CLIENT_NAME + ".*"
				+ CLIENT_NAME_CLOSE);
		Pattern findPort = Pattern.compile(PORT_OPEN + "[0-9]*"
				+ PORT_CLOSE);
		Pattern findIP = Pattern.compile(MessageTags.IP_OPEN + ".+"
				+ MessageTags.IP_CLOSE);
		if (users.matcher(msg).matches()) {
			Matcher find = findPeer.matcher(msg);
			while (find.find()) {
				String peer = find.group(0);
				String data ;
				ClientConnection client = new ClientConnection();
				Matcher findInfo = findName.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					client.setName(data.substring(13, data.length() - 14));
				}
				findInfo = findIP.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					client.setHost(findInfo.group(0).substring(5,
							data.length() - 5));
				}
				findInfo = findPort.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					client.setPort(Integer.parseInt(data.substring(6,
							data.length() - 7)));
				}   
				user.add(client);
			}
		} else
			return null;
		return user;
        }
	public static String getNameRequestChat(String msg) {
		Pattern checkRequest = Pattern.compile(CHAT_REQ_OPEN
				+ CLIENT_NAME + "[^<>]*" + CLIENT_NAME_CLOSE
				+ CHAT_REQ_CLOSE);
		if (checkRequest.matcher(msg).matches()) {
			String name = msg.substring((CHAT_REQ_OPEN + CLIENT_NAME).length(),
							msg.length() - (CLIENT_NAME_CLOSE + CHAT_REQ_CLOSE).length());
			return name;
		}
		return null;
	}
    }
}
