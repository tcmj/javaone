/* $HeadURL$
 * Copyright(c) 2005 tcmj.  All Rights Reserved. */
package com.tcmj.common;

import com.tcmj.common.swing.dialog.DialogHelper;
import java.awt.Frame;

/**
 * Version Info for com.tcmj.common
 * @author tcmj - Thomas Deutsch
 * @version $Revision$
 * @since $Date$
 */
public class VersionInfo {

    public static final String getLibTitle() {
        return "com.tcmj.common";
    }

    public static final String getVersion() {
        return "1.09.10.16";
    }

    public static final String getVersionDate() {
        return "$Date$";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Frame dlg = DialogHelper.showMessageFrame("com.tcmj.common");
            Thread.sleep(1000);


            String line = System.getProperty("line.separator");

            StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(getLibTitle()).append(line);
            sb.append("Version: ").append(getVersion()).append(line);
            sb.append("Build: ").append(getVersionDate()).append(line);
            

            DialogHelper.getInstance().displayInfoMessage(dlg, sb.toString());
            dlg.setVisible(false);
            dlg.dispose();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

}
