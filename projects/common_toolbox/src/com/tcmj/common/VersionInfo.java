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

    /** GroupId. */
    public static final String getGroupId() {
        return "com.tcmj.common";
    }

    /** Artifactid. */
    public static final String getArtifactId() {
        return "tcmj-commons";
    }

    /** Version. */
    public static final String getVersion() {
        return "2.10.03";
    }

    /** SVN-Build Date String. */
    public static final String getVersionDate() {
        return "$Date$";
    }

    /**
     * Shows Version Info Frame.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Frame frame = DialogHelper.showMessageFrame("com.tcmj.common");
            frame.setTitle(getArtifactId());
            String line = System.getProperty("line.separator");

            StringBuilder sb = new StringBuilder();
            sb.append("groupId: ").append(getGroupId()).append(line);
            sb.append("version: ").append(getVersion()).append(line);
            sb.append("build: ").append(getVersionDate()).append(line);

            DialogHelper.getInstance().displayInfoMessage(frame, sb.toString());
            frame.setVisible(false);
            frame.dispose();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

}
