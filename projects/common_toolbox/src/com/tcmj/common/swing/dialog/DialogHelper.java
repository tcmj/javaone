/* DialogHelper.java
 * Created on 26. Februar 2007, 11:21
 * Copyright(c) 2005 tcmj.  All Rights Reserved.
 * CVS: $Id: DialogHelper.java,v 1.1 2007/02/26 11:33:27 TDEUT Exp $
 * Last Modify: $Date: 2007/02/26 11:33:27 $
 */
package com.tcmj.common.swing.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides some useful procedures for working with dialogs
 * @version $Id: DialogHelper.java,v 1.1 2007/02/26 11:33:27 TDEUT Exp $
 * @author tdeut - Thomas Deutsch
 */
public final class DialogHelper {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(DialogHelper.class);
    /** Screensize. */
    public static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
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
        try {

            JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);

        } catch (HeadlessException exc) {
            logger.error("Cannot display ErrorMessage: " + exc.getMessage());
        }
    }

    /** Display Error message    */
    public void displayInfoMessage(Component parent, String msg) {
        try {

            JOptionPane.showMessageDialog(parent, msg, "Information", JOptionPane.INFORMATION_MESSAGE);

        } catch (HeadlessException exc) {
            logger.error("Cannot display InfoMessage: " + exc.getMessage());
        }
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

            DialogHelper dlghelp = DialogHelper.getInstance();

            dlghelp.displayInfoMessage("Infomessage -not modal-");

            JDialog dlg = dlghelp.showLoadingDialog();
            Thread.sleep(2000);

            dlg.setVisible(false);

            dlghelp.displayErrorMessage("Errormessage -not modal-");

        } catch (Exception exc) {
            System.out.println("E: " + exc.getMessage());
        } finally {
            System.exit(0);
        }
    }
}
