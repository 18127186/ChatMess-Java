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
public class ClientConnection {
    private String nameofClient = "";
    private String hostClient = "";
    private int portClient = 0;
    public void setPeer(String name, String host, int port) {
	nameofClient = name;
        hostClient = host;
	portClient = port;
    }
    public void setName(String name) {
        nameofClient = name;
    }
    public void setHost(String host) {
        hostClient = host;
    }
    public void setPort(int port) {
        portClient = port;
    }
    public String getName() {
        return nameofClient;
    }
    public String getHost() {
        return hostClient;
    }
   public int getPort() {
      return portClient;
    }
}
