package com.mycompany.chatmess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PageInfo {
    public static String connectId,username;
    public static String DB_URL = "jdbc:mysql://db4free.net:3306/appchatjava";
    public static String USER_NAME ="appchatjava";
    public static String PASS_WORD = "appchatjava";
    private JTextField displayname, gender, dob, personalMessage, country ,membersince ;
    private JLabel displaynameJLabel,genderJLabel,dobJLabel,personalMessageJLabel,countryJLabel,membersinceJLabel;
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
    }
    public void CreateText(){
        displayname = new JTextField();
        gender = new JTextField();
        dob = new JTextField();
        personalMessage = new JTextField();
        country = new JTextField();
        membersince = new JTextField();
        displaynameJLabel = new JLabel();
        genderJLabel = new JLabel();
        dobJLabel = new JLabel();
        personalMessageJLabel = new JLabel();
        countryJLabel = new JLabel();
        membersinceJLabel = new JLabel();
    }
    public void addComponentsToPaneEdit(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(0,1, 20, 5);
        genderJLabel.setText("Gender");
        pane.add(genderJLabel, c);
        
        CreateComponents(1, 1, 20, 500);
        pane.add(gender, c);
        
        CreateComponents(0, 2, 20, 5);
        dobJLabel.setText("Birth of day");
        pane.add(dobJLabel, c);
        
        CreateComponents(1, 2, 20, 500);
        pane.add(dob, c);
        
        CreateComponents(0, 3, 20, 5);
        personalMessageJLabel.setText("Personal Message");
        pane.add(personalMessageJLabel, c);
        
        CreateComponents(1, 3, 20, 500);
        pane.add(personalMessage, c);
        
        CreateComponents(0, 4, 20, 5);
        countryJLabel.setText("Country");
        pane.add(countryJLabel, c);
        
        CreateComponents(1, 4, 20, 500);
        pane.add(country, c);

        CreateComponents(0, 5, 20, 5);
        membersinceJLabel.setText("Member Since");
        pane.add(membersinceJLabel, c);
        
        CreateComponents(1, 5, 20, 500);
        membersince.setEditable(false);
        pane.add(membersince, c);        
        button = new JButton("Edit");
        CreateComponents(1, 6, 20, 5);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gender.getText().isEmpty() || dob.getText().isEmpty()
                        || personalMessage.getText().isEmpty() || country.getText().isEmpty())
                    JOptionPane.showMessageDialog(displayname,"Complete All The Fields");
                else{
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn =null;
                        conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
                        Statement stm = (Statement) conn.createStatement();
                        String queryCheck = "Select * from users where ID="+connectId+";";
                        ResultSet rsCheck=stm.executeQuery(queryCheck);
                        rsCheck.next();
                        String Check = rsCheck.getString("info");
                        if (Integer.parseInt(Check) == 1){
                            String queryUpdate = "UPDATE info SET displayName='"+displayname.getText()+"',Gender='"+gender.getText()+"',DoB='"+dob.getText()+"',personalMessage='"+personalMessage.getText()+
                                    "',country='"+country.getText()+"' where ID='"+connectId+"';";
                           stm.executeUpdate(queryUpdate);
                        }
                        else {
                            String queryInsert = "INSERT INTO info VALUES ('"+displayname.getText()+"','"+gender.getText()+"','"+dob.getText()+"','"+personalMessage.getText()
                                    +"','"+country.getText()+"',curdate(),'"+connectId+"');";
                            stm.executeUpdate(queryInsert);
                            String queryString = "UPDATE users SET info=1 where ID"
                                    + "='"+connectId+"';";
                            stm.executeUpdate(queryString);
                        }
                        String query2 = "UPDATE friend SET login=1 where ID='" + connectId + "';";
                        stm.executeUpdate(query2);
                        OnlineBuddy ob = new OnlineBuddy(connectId,username);
                        ob.createAndShowGUI();
                        conn.close();
                    } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println(ex.getClass().getName()+": "+ex.getMessage());
                    }
                }
            }
        });
        
        button = new JButton("Exit");
        CreateComponents(1, 8, 20, 5);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               OnlineBuddy ob;
                try {
                    ob = new OnlineBuddy(connectId,username);     
                    ob.createAndShowGUI();
                } catch (Exception ex) {
                    Logger.getLogger(PageInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });    
    }
    public void createAndShowGUIEdit() {
        
        frame1 = GUI.frame1;
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPaneEdit(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    public void addComponentsToPaneView(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(0,1, 20, 200);
        genderJLabel.setText("Gender");
        pane.add(genderJLabel, c);
        
        CreateComponents(1, 1, 20, 5);
        gender.setEditable(false);
        pane.add(gender, c);
        
        CreateComponents(0, 2, 20, 80);
        dobJLabel.setText("Birth of day");
        pane.add(dobJLabel, c);
        
        CreateComponents(1, 2, 20, 5);
        dob.setEditable(false);
        pane.add(dob, c);
        
        CreateComponents(0, 3, 20, 80);
        personalMessageJLabel.setText("Personal Message");
        pane.add(personalMessageJLabel, c);
        
        CreateComponents(1, 3, 20, 5);
        
        personalMessage.setEditable(false);
        pane.add(personalMessage, c);
        
        CreateComponents(0, 4, 20, 80);
        countryJLabel.setText("Country");
        pane.add(countryJLabel, c);
        
        CreateComponents(1, 4, 20, 5);
        country.setEditable(false);
        pane.add(country, c);

        CreateComponents(0, 5, 20, 80);
        membersinceJLabel.setText("Member Since");
        pane.add(membersinceJLabel, c);
        
        CreateComponents(1, 5, 20, 5);
        membersince.setEditable(false);
        pane.add(membersince, c);               
        button = new JButton("Exit");
        CreateComponents(1, 8, 20, 80);
        pane.add(button, c);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OnlineBuddy ob = new OnlineBuddy();
                } catch (Exception ex) {
                    Logger.getLogger(PageInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

       
        
    }
    public void createAndShowGUIView() {
        
        frame1 = GUI.frame1;
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addComponentsToPaneView(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    public PageInfo(String a){
        connectId = a;
        CreateText();
        try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn =null;
                    conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
                    Statement stm = (Statement) conn.createStatement();
                    String querySearchAccount = "SELECT * FROM users , info WHERE users.ID = info.ID and users.ID='"+connectId + "';";
                    ResultSet rs = stm.executeQuery(querySearchAccount);
                    if (rs.next()){
                        username = rs.getString("userName");
                        gender.setText(rs.getString("Gender"));
                        dob.setText(rs.getString("DoB"));
                        personalMessage.setText(rs.getString("personalMessage"));
                        country.setText(rs.getString("country"));
                        membersince.setText(rs.getString("memberSince"));
                    }
                    else{
                        String query = "SELECT userName FROM users WHERE ID = "+connectId + ";";
                        ResultSet rs1 = stm.executeQuery(query);
                        rs1.next();
                        username = rs1.getString("userName");
                        displayname.setText(username);
                        gender.setText("");
                        dob.setText("");
                        personalMessage.setText("");
                        country.setText("");
                        membersince.setText("");
                    }
                    conn.close();
        } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getClass().getName()+": "+ex.getMessage());
        }
    }

}
