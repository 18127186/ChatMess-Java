/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatmess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author trand
 */
public class Register {
    public static String DB_URL = "jdbc:mysql://localhost:3309/appjava";
    public static String USER_NAME ="root";
    public static String PASS_WORD = "Trandinhphuoc2k";
    private JTextField name,userName,passWord,confirmPassword,email;
    private JLabel nameJLabel,userNameJLabel,passWordJLabel,confirmPassWordJLabel,emailJLabel;
    public JFrame frame1;
    public   JButton button;
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
        name = new JTextField();
        userName = new JTextField();
        passWord = new JPasswordField("",15);
        confirmPassword = new JPasswordField("",15);
        email = new JTextField();
        nameJLabel = new JLabel();
        userNameJLabel = new JLabel();
        passWordJLabel = new JLabel();
        confirmPassWordJLabel = new JLabel();
        emailJLabel = new JLabel();
    }
    public void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(0, 0, 20, 200);
        nameJLabel.setText("Name");
        pane.add(nameJLabel, c);
        
        CreateComponents(1, 0, 20, 5);
        pane.add(name, c);
        
        CreateComponents(0,1, 20, 200);
        userNameJLabel.setText("UserName");
        pane.add(userNameJLabel, c);
        
        CreateComponents(1, 1, 20, 5);
        userName.setText("");
        pane.add(userName, c);
        
        CreateComponents(0, 2, 20, 80);
        passWordJLabel.setText("PassWord");
        pane.add(passWordJLabel, c);
        
        CreateComponents(1, 2, 20, 5);
        passWord.setText("");
        pane.add(passWord, c);
        
        CreateComponents(0, 3, 20, 80);
        confirmPassWordJLabel.setText("Confirm PassWord");
        pane.add(confirmPassWordJLabel, c);
        
        CreateComponents(1, 3, 20, 5);
        confirmPassword.setText("");
        pane.add(confirmPassword, c);
        
        CreateComponents(0, 4, 20, 80);
        emailJLabel.setText("Email");
        pane.add(emailJLabel, c);
        
        CreateComponents(1, 4, 20, 5);
        email.setText("");
        pane.add(email, c);        
        
        button = new JButton("Sign up");
        CreateComponents(1, 6, 20, 80);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(userName, "Enter the Username");
                } else if(passWord.getText().isEmpty()){
                    JOptionPane.showMessageDialog(passWord, "Enter the PassWord");
                } else if(name.getText().isEmpty()){
                    JOptionPane.showMessageDialog(name, "Enter the Name");
                } else if(confirmPassword.getText().isEmpty()){
                    JOptionPane.showMessageDialog(confirmPassword, "Enter the Confirm PassWord");
                } else if(email.getText().isEmpty()){
                    JOptionPane.showMessageDialog(email, "Enter the Email");
                } else if(confirmPassword.getText().equals(passWord.getText()) == false){
                    JOptionPane.showMessageDialog(passWord, "Confirm PassWord different with PassWord");
                }
                else{
                    Connection conn =null;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
                        Statement stm = (Statement) conn.createStatement();
                        String querySearchAccount = "SELECT ID FROM users WHERE userName='"+userName.getText()+"';";
                        ResultSet rs = stm.executeQuery(querySearchAccount);
                        if(rs.next()){
                            JOptionPane.showMessageDialog(userName, "Account already exists");
                        }
                        else{
                            String  queryAddAccount ="INSERT INTO users VALUES (0,'"+userName.getText()+"',md5('"+passWord.getText()+"'),123,'"+email.getText()+"','"+name.getText()+"','123',1,0);";
                            stm.executeUpdate(queryAddAccount);
                            String query="SELECT ID  FROM users  where  username= '"+userName.getText()+"';";
                            ResultSet rsSelect=stm.executeQuery(query);

                            if (rsSelect.next()){ 
                                int id = rsSelect.getInt("ID");      
                                String queryBuddy="INSERT INTO buddy VALUES( 0,'"+id+"','"+userName.getText()+"',curtime(),0,0);";
                                stm.executeUpdate(queryBuddy);    
                            }
                            JOptionPane.showMessageDialog(name,"Registration Completed");
                            GUI gui = new GUI();
                            gui.createAndShowGUI();
                            conn.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getClass().getName()+": "+ex.getMessage());
                    }
                }
            }
        });
        
        button = new JButton("Exit");
        CreateComponents(1, 8, 20, 80);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               GUI gui = new GUI();
               gui.createAndShowGUI();
            }
        });
        //button.addActionListener(this);

       
        
    }
    public void createAndShowGUI() {
        
        frame1 = GUI.frame1;
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CreateText();
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
}
