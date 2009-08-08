package com.tcmj.common.swing.dialog;

import java.awt.Component;
import java.awt.Frame;
import javax.swing.JDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DialogHelperTest.
 * @author tcmj
 */
public class DialogHelperTest {

    public DialogHelperTest() {
    }

    /**
     * Test of getInstance method, of class DialogHelper.
     */
    @Test
    public void testGetInstance() {
        assertNotNull(DialogHelper.getInstance());
    }

    /**
     * Test of showMessageDialog method, of class DialogHelper.
     */
    @Test
    public void testShowMessageDialog() throws InterruptedException {
        System.out.println("showMessageDialog");
        JDialog jdlg = DialogHelper.showMessageDialog(" L o a d i n g...");
        Thread.sleep(1000);
        assertNotNull(jdlg);
        jdlg.setVisible(false);
    }

    /**
     * Test of showMessageFrame method, of class DialogHelper.
     */
    @Test
    public void testShowMessageFrame() throws InterruptedException {
        System.out.println("showMessageFrame");
        Frame dlg = DialogHelper.showMessageFrame(" L o a d i n g...");
        Thread.sleep(1000);
        assertNotNull(dlg);
        dlg.setVisible(false);
    }

}
