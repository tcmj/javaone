/* DialogHelper.java
 * Created on 26. Februar 2007, 11:21
 * Copyright(c) 2005 tcmj.  All Rights Reserved.
 * CVS: $Id: DialogHelper.java,v 1.1 2007/02/26 11:33:27 TDEUT Exp $
 * Last Modify: $Date: 2007/02/26 11:33:27 $
 */
package com.tcmj.common.swing.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This class provides some useful procedures for working with dialogs
 * @version $Id: DialogHelper.java,v 1.1 2007/02/26 11:33:27 TDEUT Exp $
 * @author tdeut - Thomas Deutsch
 */
public final class DialogHelper {

    /** Screensize. */
    public static final Dimension SCREENSIZE =
            Toolkit.getDefaultToolkit().getScreenSize();

    /** Singleton. */
    private static final DialogHelper singleton = new DialogHelper();

    /** Singleton ! */
    private DialogHelper() {
    }

    /**Singleton.
     * @return DialogHelper instance
     */
    public static final DialogHelper getInstance() {
        return singleton;
    }

    /** Display Error message.    */
    public void displayErrorMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Display Error message    */
    public void displayInfoMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Display Error message    */
    public void displayErrorMessage(String msg) {
        displayErrorMessage(null, msg);
    }

    /** Display Error message    */
    public void displayInfoMessage(String msg) {
        displayInfoMessage(null, msg);
    }

    /**
     * Display the loading dialog.
     * @return JDialog to dispose
     */
    public static final JDialog showLoadingDialog(final String message) {
        final javax.swing.JDialog dlgloading = new javax.swing.JDialog();
        new Thread() {

            @Override
            public void run() {

                setPriority(Thread.MAX_PRIORITY);

                final javax.swing.JLabel label = new javax.swing.JLabel(" " + message);

                dlgloading.setUndecorated(true);

                dlgloading.getContentPane().add(label);

                int width = (message.length() * 6) + 25;  // chars * 6 pixel + puffer

                dlgloading.setBounds((SCREENSIZE.width - width) / 2, (SCREENSIZE.height - 20) / 2, width, 20);

                dlgloading.setVisible(true);
            }

        }.start();
        return dlgloading;
    }

    static class DrawingWindow extends Frame {

        String title = "";

//        int width;
//        int height;
//        int xpos;
//        int ypos;

        DrawingWindow(String title) {
            super();
            this.title = title;
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawString(title, 5, 12);
        }

    }

    /**
     * Display the loading dialog.
     * @return JDialog to dispose
     */
    public static final Frame showLoad(final String message) {
        final Frame dlgloading = new DrawingWindow(message);

        new Thread() {

            @Override
            public void run() {
                setPriority(Thread.MAX_PRIORITY);
                dlgloading.setUndecorated(true);
                dlgloading.setBackground(Color.LIGHT_GRAY);
                int width = (message.length() * 6) + 10;
                int height = 16;
                int xpos = (SCREENSIZE.width - width) / 2;
                int ypos = (SCREENSIZE.height - 20) / 2;
                dlgloading.setBounds(xpos, ypos, width, height);
                dlgloading.setVisible(true);
            }

        }.start();
        return dlgloading;
    }

    /**
     * Display the loading dialog.
     * @return JDialog to dispose
     */
    public static final JDialog showLoadingDialog() {
        return showLoadingDialog("Loading...");
    }

    /** centers a frame, dialog or something else on the screen */
    public static void centerComponent(Component com) {

        com.setLocation((SCREENSIZE.width - com.getSize().width) / 2,
                (SCREENSIZE.height - com.getSize().height) / 2);

    }

    /**Example using this class
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

//            Toolkit.getDefaultToolkit().
//            SplashScreen splash = SplashScreen.getSplashScreen();


            DialogHelper dlghelp = DialogHelper.getInstance();

            dlghelp.displayInfoMessage("Infomessage -not modal-");

//            JDialog dlg = DialogHelper.showLoadingDialog();
            long start = System.currentTimeMillis();
            Frame dlg = DialogHelper.showLoad("Errormessage -not modal-");
            long end = System.currentTimeMillis();
            System.out.println("time dlg = "+(end-start));

            Thread.sleep(9000);

            dlg.setVisible(false);

            long start2 = System.currentTimeMillis();
            JDialog jdlg = DialogHelper.showLoadingDialog();
            long end2 = System.currentTimeMillis();
            System.out.println("time dlg2 = "+(end2-start2));

            Thread.sleep(6000);
            jdlg.setVisible(false);
//            dlghelp.displayErrorMessage("Errormessage -not modal-");

        } catch (Exception exc) {
            System.out.println("E: " + exc.getMessage());
        } finally {
            System.exit(0);
        }
    }

}
