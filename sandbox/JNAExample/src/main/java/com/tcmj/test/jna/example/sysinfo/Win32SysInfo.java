/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.test.jna.example.sysinfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jna.*;
import com.sun.jna.win32.*;

/**
 * Utility method to retrieve the idle time on Windows and sample code to test it.
 * JNA shall be present in your classpath for this to work (and compile).
 * @author ochafik
 */
public class Win32SysInfo {

    public interface Kernel32 extends StdCallLibrary {

        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

//        String  lpExistingFileName;
//  String lpNewFileName;
//  boolean  bFailIfExists;
        public int CopyFile();
    };

    public interface User32 extends StdCallLibrary {

        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

        interface WNDENUMPROC extends StdCallCallback {

            /** Return whether to continue enumeration. */
            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
    };

    /**
     * Get the amount of milliseconds that have elapsed since the last input event
     * (mouse or keyboard)
     * @return idle time in milliseconds
     */
    public static int getIdleTimeMillisWin32() {
        User32 user32 = User32.INSTANCE;

        user32.EnumWindows(new User32.WNDENUMPROC() {

            int count;

            public boolean callback(Pointer hWnd, Pointer userData) {
                System.out.println("Found window " + hWnd + ", total " + ++count);
                return true;
            }
        }, null);
        return 66;
    }

    enum State {

        UNKNOWN, ONLINE, IDLE, AWAY
    };

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("Windows")) {
            System.err.println("ERROR: Only implemented on Windows");
            System.exit(1);
        }
        State state = State.UNKNOWN;
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
//int idleSec = getIdleTimeMillisWin32() / 1000;


        System.out.println("... " + getIdleTimeMillisWin32());

    }
}
