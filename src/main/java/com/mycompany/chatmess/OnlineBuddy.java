
package com.mycompany.chatmess;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class OnlineBuddy {
    public static String connectId,user;
    public static String DB_URL = "jdbc:mysql://localhost:3309/appjava";
    public static String USER_NAME ="root";
    public static String PASS_WORD = "Trandinhphuoc2k";
    public JFrame frame1;
    public JButton button;
    public JLabel userField;
    final static boolean shouldFill = true;
    final static boolean RIGHT_TO_LEFT = false;
    public GridBagConstraints c = new GridBagConstraints();
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
    //create on GridBagConstraints c
    public void CreateComponents(int gridx,int gridy, int ipady,int ipadx){
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = new Insets(5,5,5,5); 
        c.ipady = ipady; 
        c.ipadx = ipadx;  
        c.weighty = 0.1;
    }
    public void CreateText(){
        userField = new JLabel();
        userField.setText(user);
        userField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                PageInfo pi = new PageInfo(connectId);
                pi.createAndShowGUIEdit();
            }
        });
    }
     public void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(0, 0, 10, 200);
        pane.add(userField, c);
        button = new JButton("Sign out");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI gui = new GUI();
                gui.createAndShowGUI();
            }
        });
        CreateComponents(2, 0, 10, 5);
        pane.add(button, c);

       
        
    }
    public void createAndShowGUI() {
        
        frame1 = GUI.frame1;
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    public OnlineBuddy(String userId,String userName){
        connectId = userId;
        user = userName;
        CreateText();
        createAndShowGUI();
    }
}
