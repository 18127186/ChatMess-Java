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

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
public class ConnectionDB extends JFrame {
    public static String connectID;
   
    public static Connection conn;
    public static JTextField noti;
    public static String DB_URL = "jdbc:mysql://db4free.net:3306/appchatjava";
    public static String USER_NAME ="appchatjava";
    public static String PASS_WORD = "appchatjava";

    public ConnectionDB(String userName, String passWord){
        Connection conn = getConnection(DB_URL, USER_NAME, PASS_WORD,userName,passWord);
    }
    public static Connection getConnection(String dbURL,String userName, String passWord,String usernametoLogin, String passWordtoLogin) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(dbURL + "?user=" + userName+ "&password=" + passWord );
            Statement stm = (Statement) conn.createStatement();
            String query1 = "SELECT ID,info, passWord,verify ,md5('" + passWordtoLogin + "') as curpass FROM users WHERE userName='" + usernametoLogin + "';";
            ResultSet rs1 = stm.executeQuery(query1);
            if (rs1.next()) {
                    String pass = rs1.getString("passWord");
                    String curpass = rs1.getString("curpass");
                    int verify = rs1.getInt("verify");
                    int info = rs1.getInt("info");
                    int userid = rs1.getInt("ID");
                    connectID =String.valueOf(userid);
                    if (pass.equals(curpass) && (verify == 1)) {
                        JOptionPane.showMessageDialog(noti, "Login success");
                        String queryToSearchName = "SELECT userName as displayName FROM users WHERE ID ='" + userid + "';";
                        ResultSet rs = stm.executeQuery(queryToSearchName);
                        String userDisplayName = "";
                        if(rs.next()) userDisplayName = rs.getString("displayName");
                        OnlineBuddy ob = new OnlineBuddy(connectID,userDisplayName);
                        if (info == 1) {
                            try {
                                String query2 = "UPDATE friend SET login=1 where ID='" + userid + "';";
                                stm.executeUpdate(query2);
                                ob.createAndShowGUI();
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(noti, e.getMessage());
                            }
                        } else {
                            PageInfo pi = new PageInfo(connectID);
                            pi.createAndShowGUIEdit();
                            
                        }
                    } else {
                        JOptionPane.showMessageDialog(noti, "Invalid Username Or Password");
                        return null;
                    }
 
                } // if (rs1.next()&&rs2.next())
                else {
                    JOptionPane.showMessageDialog(noti, "User does not exist");
                    return null;
                }
 
                conn.close();
            System.out.println("Access Sucessfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.out.println("Kết nối thất bại!");
        }
        return conn;
    }
}
