/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatmess;

import java.io.File;
/**
 *
 * @author trand
 */
public class Main {
    
    public static void main(String[] args) {
        GUI UI = new GUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UI.createAndShowGUI();
            }
        });
    }
}
