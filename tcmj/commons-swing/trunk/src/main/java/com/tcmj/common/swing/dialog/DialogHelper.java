/* $HeadURL: https://tcmj.googlecode.com/svn/trunk/projects/common_toolbox/src/com/tcmj/common/swing/dialog/DialogHelper.java $
 * SVN: $Id: DialogHelper.java 52 2010-04-01 11:42:50Z tomdeu $
 * Created on 26. Februar 2007, 11:21
 * Copyright(c) 2005 tcmj.  All Rights Reserved.
 * Last Modify: $Date: 2010-04-01 13:42:50 +0200 (Do, 01 Apr 2010) $
 */
package com.tcmj.common.swing.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This class provides some useful procedures for working with dialogs.
 * @version $Id: DialogHelper.java 52 2010-04-01 11:42:50Z tomdeu $
 * @author tdeut - Thomas Deutsch
 * @JUnit com.tcmj.common.swing.dialog.DialogHelperTest
 */
public final class DialogHelper {

    /** Screensize. */
    public static final Dimension SCREENSIZE =
            Toolkit.getDefaultToolkit().getScreenSize();

    /** Singleton. */
    private static final DialogHelper singleton = new DialogHelper();

    /** Singleton. */
    private DialogHelper() {
    }

    /** Singleton.
     * @return DialogHelper instance
     */
    public static final DialogHelper getInstance() {
        return singleton;
    }

    /** Display Error message.
     * @param parent parent frame/component
     * @param msg message to show
     */
    public void displayErrorMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Display Error message.
     * @param parent parent frame/component
     * @param msg message to show
     */
    public void displayInfoMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Display Error message.
     * @param msg message to show
     */
    public void displayErrorMessage(String msg) {
        displayErrorMessage(null, msg);
    }

    /** Display Error message.
     * @param msg message to show
     */
    public void displayInfoMessage(String msg) {
        displayInfoMessage(null, msg);
    }

    /**
     * Display the loading dialog.
     * @param message text to show
     * @return JDialog to dispose
     */
    public static final JDialog showMessageDialog(final String message) {
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

    /**
     * Display the loading dialog.
     * @param message text to show
     * @return JDialog to dispose
     */
    public static final Frame showMessageFrame(final String message) {
        final Frame dlgloading = new DrawingWindow(message);

        new Thread() {

            @Override
            public void run() {
                setPriority(Thread.MAX_PRIORITY);
                dlgloading.setUndecorated(true);
                dlgloading.setBackground(Color.LIGHT_GRAY);
                int width = (message.length() * 6) + 20;
                int height = 16;
                int xpos = (SCREENSIZE.width - width) / 2;
                int ypos = (SCREENSIZE.height - 20) / 2;
                dlgloading.setBounds(xpos, ypos, width, height);
                dlgloading.setVisible(true);
            }

        }.start();
        return dlgloading;
    }

    /** centers a frame, dialog or something else on the screen.<br>
     * formula: (Screen.Width - Component.Width / 2) and (Screen.Height - Component.Height / 2)
     * @param com frame or component
     */
    public static void centerComponent(Component com) {
        com.setLocation((SCREENSIZE.width - com.getSize().width) / 2,
                (SCREENSIZE.height - com.getSize().height) / 2);
    }

    /** internal used Frame class. */
    private static class DrawingWindow extends Frame {

        String title;

        DrawingWindow(String title) {
            this.title = title;
        }

        @Override
        public void paint(Graphics g) {
            g.drawString(title, 5, 12);
        }

    }
}
