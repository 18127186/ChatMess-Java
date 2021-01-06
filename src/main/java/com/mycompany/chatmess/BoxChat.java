/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatmess;

import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author trand
 */
public class BoxChat {
    public Room chatRoom;
    public Socket socketChat;
    private String user, Guest;
    public int portServer;
    public JFrame frame1;
    public JButton button,buttonsend;
    public JLabel userField;
    public JScrollPane scrollpanefortextchat,scrollpanefortextwrite;
    public JTextArea textareaforChat,textAreaforWrite;
    static boolean shouldFill = true;
    public boolean Stop = true;
    public static String msgforChat;
    public GridBagConstraints c = new GridBagConstraints();
    public void FirstOfAddComponents(Container pane){
        pane.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;

    }
    //create on GridBagConstraints c
    public void CreateComponents(int gridx,int gridy, int ipady,int ipadx){
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = new Insets(5,5,5,5); 
        c.ipady = ipady; 
        c.ipadx = ipadx;  
    }
    public void CreateText(){
        buttonsend = new JButton("Send");
        button = new JButton("Leave");
        textareaforChat = new JTextArea(5, 20);
        scrollpanefortextchat = new JScrollPane(textareaforChat);
        textAreaforWrite = new JTextArea(2, 15);
        scrollpanefortextwrite = new JScrollPane(textAreaforWrite);
        userField = new JLabel(Guest);
    }
     public void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);
        CreateText();
        CreateComponents(2, 0, 10, 200);
        pane.add(userField, c);
        
        CreateComponents(0, 2, 10, 200);
        c.gridwidth = 6;
        textareaforChat.setEditable(false);
        pane.add(scrollpanefortextchat,c);
        
        CreateComponents(0, 7, 10, 200);
        c.gridwidth = 3;
        pane.add(scrollpanefortextwrite,c);
        
        buttonsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = textAreaforWrite.getText().trim();
                try {
                    chatRoom.send(msg);
                    textAreaforWrite.setText("");
                } catch (Exception ex) {
                    Logger.getLogger(BoxChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        CreateComponents(3, 7, 10, 5);
        pane.add(buttonsend, c);
        
        
        textAreaforWrite.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    buttonsend.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int req = MessageTags.show(frame1,"Do you want to leave chat !!!",true);
                if(req == 0){
                    Stop = false;
                    try {
                        chatRoom.send(MessageTags.CHAT_CLOSE_TAG);
                        frame1.dispose();
                        socketChat.close();
                    } catch (Exception ex) {
                        Logger.getLogger(BoxChat.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.gc();
                }
            }
        });
        CreateComponents(3, 0, 10, 5);
        pane.add(button, c);
        
       
        
    }
    public void createAndShowGUI() {
        
        frame1 = new JFrame(Guest);
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    BoxChat(String nameUser, String usernameClick, Socket connclient) throws Exception {
        this.user = nameUser;
        this.Guest =  usernameClick;
        this.socketChat = connclient;
        createAndShowGUI();
        chatRoom = new Room(connclient, nameUser, usernameClick);
        chatRoom.start();
    }
    class Room extends Thread{
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    
    public Room(Socket conectionSocket, String name, String guestString ) throws Exception{
        socket = new Socket();
        socket = conectionSocket;
        Guest = guestString;
    }
    @Override
    public void run(){
        super.run();
        OutputStream os = null;
        while (Stop) {            
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                String msg = (String) ois.readObject();
                if(msg.equals(MessageTags.CHAT_CLOSE_TAG)){
                    Stop = false;
                    MessageTags.show(frame1, Guest + " closed chat ", false);
                    frame1.dispose();
                    socket.close();
                    System.gc();
                    break;
                }
                else{
                    textareaforChat.append("\n"+ Guest +" : " + msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // synchronized tranh cho truong hop qua nhieu thread cung truy cap vao 1 cai gi do
    public  synchronized void send(String msg) throws Exception{
        oos = new ObjectOutputStream(socket.getOutputStream());
	oos.writeObject(msg);
	oos.flush();
        textareaforChat.append("\n"+ user +" : " + msg);
    }
 }

}
