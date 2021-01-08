
package com.mycompany.chatmess;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.table.DefaultTableModel;

public class OnlineBuddy {
    // chat
    
    private Client clientNode;
    Socket socket;
    public String ip;
    public int portClient;
    public int portServer = 1234;
    //
    public String usernameClick,useridClick;
    public static String connectId,user;
    public static String ipOfserver = GUI.ipofServer;
    public static String DB_URL = "jdbc:mysql://db4free.net:3306/appchatjava";
    public static String USER_NAME ="appchatjava";
    public static String PASS_WORD = "appchatjava";
    public JFrame frame1;
    public JButton button;
    public JLabel userField;
    public JTextField notiField;
    
    public   JButton buttonviewPage,buttonChat;
    
    public static JList listnamefriend,listidfriend;
    public JScrollPane scroll;
    public static JTable table;
    final static boolean shouldFill = true;
    final static boolean RIGHT_TO_LEFT = false;
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
        
        notiField = new JTextField();
        userField = new JLabel();
        userField.setText(user);
        userField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                PageInfo pi = new PageInfo(connectId);
                pi.createAndShowGUIEdit();
            }
        });
        listidfriend = new JList();
        listnamefriend = new JList();
        scroll = new JScrollPane();
        scroll.setViewportView(listnamefriend);
        listnamefriend.setLayoutOrientation(JList.VERTICAL);
    }
     public void addComponentsToPane(Container pane) {
        FirstOfAddComponents(pane);
        CreateComponents(0, 0, 10, 200);
        pane.add(userField, c);
        
        button = new JButton("Sign out");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn =null;
                    conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
                    Statement stm = (Statement) conn.createStatement();
                    String query2 = "UPDATE friend SET login=0 where ID='" + connectId + "';";
                    stm.executeUpdate(query2);
                    conn.close();
                    // ngat ket noi socket
                    int result = MessageTags.show(frame1, "Are you sure ?", true);
                    if (result == 0) {
			try {
                            clientNode.exit();
                            frame1.dispose();
			} catch (Exception ex) {
                            frame1.dispose();
			}
                        
                        GUI gui = new GUI();
                        gui.createAndShowGUI();
                    }
                    //
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println(ex.getClass().getName()+": "+ex.getMessage());
                };
            }
        });
        CreateComponents(3, 0, 10, 5);
        pane.add(button, c);
        
         CreateComponents(0, 1, 10, 5);
         pane.add(scroll,c);
         listnamefriend.addMouseListener(new MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listnamefriendMouseClicked(evt);
            }
        });
       
        
    }
    public void createAndShowGUI() {
        
        frame1 = GUI.frame1;
        frame1.getContentPane().removeAll();
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addComponentsToPane(frame1.getContentPane());
        
        frame1.pack();
        frame1.setVisible(true);
    }
    public OnlineBuddy() throws Exception{
        OnlineBuddy ob = new OnlineBuddy(connectId,user);
        
    }
    public OnlineBuddy(String userId,String userName) throws Exception{
        connectId = userId;
        user = userName;
        //ip = Inet4Address.getLocalHost().getHostAddress(); // dia chi ip 
        ip = ipOfserver; // ip cua server
        Random rd = new Random();
        portClient = 10000 + rd.nextInt() % 1000;
        InetAddress ipServer = InetAddress.getByName(ip);
        portServer = 1234;
        Socket socketClient = new Socket(ipServer, portServer);
        
        
        String msg = MessageTags.Encode.getCreateAccount(user, Integer.toString(portClient));
        ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
        serverOutputStream.writeObject(msg);
        serverOutputStream.flush();
        ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
        msg = (String) serverInputStream.readObject();

        socketClient.close();
        clientNode = new Client(ip, portClient, user, msg);
        // da toi day
        CreateText();
        createAndShowGUI();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
             Connection conn =null;
             conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
             Statement stm = (Statement) conn.createStatement();
             int sizeofFriend =0;
            String querySelectsize = "SELECT count(ID) as sizearray  FROM friend  where login=1  and ID not in (" + connectId + "); ";
                    ResultSet rsSelectSize = stm.executeQuery(querySelectsize);
                    if (rsSelectSize.next()) {
                        sizeofFriend = rsSelectSize.getInt("sizearray");
                    } 
                    String arrayDisplayName[] = new String[sizeofFriend];
                    String arrayID[] = new String[sizeofFriend];
                    
                    String querySelectfriend = "SELECT friend.ID,info.displayName,users.userName  FROM friend , info, users where  info.ID=friend.ID and friend.ID = users.ID and friend.login=1  and friend.ID not in (" + connectId + ") ;";
                    ResultSet rs = stm.executeQuery(querySelectfriend);
                    
             
                    int idToAddList;
                    int index = 0;
                    String DisplayName;
                    while(rs.next()){
                        idToAddList = rs.getInt("ID");
                        DisplayName = rs.getString("userName");
                        arrayDisplayName[index] = DisplayName;
                        arrayID[index] = String.valueOf(idToAddList);
                        ++index;
                    }
                    listidfriend.setListData(arrayID);
                    listnamefriend.setListData(arrayDisplayName);
                    conn.close();
            
                    
        } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println(ex.getClass().getName()+": "+ex.getMessage());
        }
    }
    private void listnamefriendMouseClicked(MouseEvent evt) {
        
        usernameClick = (String) listnamefriend.getSelectedValue();
        listidfriend.setSelectedIndex(listnamefriend.getSelectedIndex());
        useridClick = (String) listidfriend.getSelectedValue(); 
        if(buttonviewPage != null) buttonviewPage.removeAll();
        if(buttonChat != null) buttonChat.removeAll();
        buttonviewPage = new JButton("View Page");
        CreateComponents(3, 1, 1, 5);
        buttonviewPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageInfo pi = new PageInfo(useridClick);
                pi.createAndShowGUIView();
            }
        });
        frame1.getContentPane().add(buttonviewPage,c);
        buttonChat = new JButton("Request Chat");
        CreateComponents(4, 1, 1, 5);
        buttonChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                // lay lai danh sach client array
                //ip = Inet4Address.getLocalHost().getHostAddress(); // dia chi ip cua mang
                ip = ipOfserver; //ip cua server
                InetAddress ipServer = InetAddress.getByName(ip);
                int portServer = 1234;
                Socket socketClient = new Socket(ipServer, portServer);


                String msg = MessageTags.Encode.getCreateAccount(user, Integer.toString(portClient));
                ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
                serverOutputStream.writeObject(msg);
                serverOutputStream.flush();
                ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
                msg = (String) serverInputStream.readObject();

                socketClient.close();
                clientNode.clientarray = MessageTags.Decode.getAllUser(msg);
                String usernameClick,useridClick;
                usernameClick = (String) listnamefriend.getSelectedValue();
                listidfriend.setSelectedIndex(listnamefriend.getSelectedIndex());
                useridClick = (String) listidfriend.getSelectedValue(); 
                if (useridClick == null) {} //Click nham vao cho k co list
                else{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn =null;
                    conn = (Connection) DriverManager.getConnection(DB_URL + "?user=" + USER_NAME+ "&password=" + PASS_WORD );
                    Statement stm = (Statement) conn.createStatement();
                    String queryCheckLogin = "SELECT login  FROM friend  where ID =" + useridClick + "; ";
                    ResultSet rsCheckLogin = stm.executeQuery(queryCheckLogin);
                    rsCheckLogin.next();
                    int checkLogin = rsCheckLogin.getInt("login");
                    if(checkLogin == 0) {
                        JOptionPane.showMessageDialog(notiField, "User is offline");
                    }
                    else{
                        int size = Client.clientarray.size();
                           for (int i = 0; i < size; i++) {
                               String s = Client.clientarray.get(i).getName();
                                if (usernameClick.equals(s)) {
                                    try {
                                        clientNode.intialNewChat(Client.clientarray.get(i).getHost(),Client.clientarray.get(i).getPort(), usernameClick);
                                        return;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                            }
			}     
                    }

                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println(ex.getClass().getName()+": "+ex.getMessage());
            }
            }
        });
        frame1.getContentPane().add(buttonChat,c);
        frame1.pack();
        frame1.setVisible(true);
    }
    public static int Request(String msg, boolean type){
		JFrame frameMessage = new JFrame();
		return MessageTags.show(frameMessage, msg, type);
    }
    
}
