

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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GUI {
    public static String connectID;
    final static boolean shouldFill = true;
    final static boolean RIGHT_TO_LEFT = false;
    public static String ipofServer;
    public static JFrame frame1 = new JFrame("Chat Mess");
    public GridBagConstraints c = new GridBagConstraints();
    public   JButton button;
    public  JTextField NameApp,userName,passWord,ipserver;
    public JLabel userNameNotifi,passWordNotifi,IPNotifi;
    public JTextField notification;
    public void FirstOfAddComponents(Container pane){
        if (RIGHT_TO_LEFT) 
            {
                pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            }
        pane.setLayout(new GridBagLayout());
        if (shouldFill) 
            {
                c.fill = GridBagConstraints.HORIZONTAL;
            }
    }
    public void CreateText(){
        NameApp = new JTextField();
        userName = new JTextField();
        passWord = new JPasswordField("",15);
        userNameNotifi = new JLabel();
        passWordNotifi = new JLabel();
        ipserver = new JTextField(); // nhap ip server de ket noi
        IPNotifi = new JLabel("Ip Server");
    }
    //create on GridBagConstraints c
    public void CreateComponents(int gridx,int gridy, int ipady,int ipadx){
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = new Insets(5,5,5,5); 
        c.ipady = ipady; 
        c.ipadx = ipadx; 
    }
    public void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(1, 0, 20, 200);
        NameApp.setText("Chat Messenger");
        NameApp.setEditable(false);
        pane.add(NameApp, c);
        
        CreateComponents(0, 1, 20, 5);
        userNameNotifi.setText("User Name: ");
        pane.add(userNameNotifi, c);
        
        CreateComponents(1,1, 20, 200);
        userName.setText("");
        pane.add(userName, c);
        
        CreateComponents(0, 2, 20, 5);
        passWordNotifi.setText("Pass Word: ");
        pane.add(passWordNotifi, c);
        
        CreateComponents(1, 2, 20, 80);
        passWord.setText("");
        pane.add(passWord, c);
        
        button = new JButton("Sign in");
        CreateComponents(1, 5, 20, 80);
        pane.add(button, c);
        button.setActionCommand("Sign in");   
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(userName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(userName, "Enter the Username");
                } else if(passWord.getText().isEmpty()){
                    JOptionPane.showMessageDialog(passWord, "Enter the PassWord");
                }
                else{
                    ipofServer = ipserver.getText().trim();
                    ConnectionDB connection = new ConnectionDB(userName.getText(),passWord.getText());
                }
            }
        });
        
        button = new JButton("Sign up");
        CreateComponents(1, 6, 20, 80);
        pane.add(button, c);
        button.setActionCommand("Sign up");  
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    Register rg = new Register();
                    rg.createAndShowGUI();
            }
        });
        
        button = new JButton("Forgot password");
        CreateComponents(1, 7, 20, 80);
        pane.add(button, c);
        button.setActionCommand("Forgot password");  
        
        button = new JButton("Exit");
        CreateComponents(1, 8, 20, 80);
        pane.add(button, c);
        button.setActionCommand("Exit"); 
        
        //button.addActionListener(this);
        
        CreateComponents(0,9, 20, 80);
        pane.add(IPNotifi,c);
        
        CreateComponents(1,9, 20, 80);
        pane.add(ipserver,c);
        
            
    }
    public void createAndShowGUI() {
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CreateText();
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    
  
}
