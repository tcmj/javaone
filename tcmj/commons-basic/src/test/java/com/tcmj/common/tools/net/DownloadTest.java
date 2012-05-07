/*
 * Copyright (C) 2012 tcmj development
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.net;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author tcmj
 */
public class DownloadTest {

    @Test
    public final void shouldDownloadAFileFromAnUrlToASpecificLocation() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlToASpecificLocation");
        
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar");

        File myfile = new File(System.getProperty("user.dir"), "myjarfile.jar");

        Download.aFile(url, myfile);

        checkFile(myfile);

        try { //to cleanup
            myfile.delete();
        } catch (Exception e) {
        }

    }

    @Test
    public final void shouldDownloadAFileFromAnUrlAsTempFile() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlAsTempFile");
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar");
        File downloadedFile = Download.aFile(url);   //file name w
        checkFile(downloadedFile);
    }
    
    
    @Test
    public final void shouldDownloadAFileFromAnUrlWithParameters() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlWithParameters");
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar?user=max&name=mutzke");
        File downloadedFile = Download.aFile(url);   //file name w
        checkFile(downloadedFile);
    }
    
    @Test
    public final void shouldDownloadAHtmlFileFromAnUrlWithParameters() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlWithParameters");
        URL url = new URL("http://www.theserverside.com/discussions/thread.tss?thread_id=32379");
        File downloadedFile = Download.aFile(url);   //file name w
        checkFile(downloadedFile);
    }

    /**
     * internal assertion method used by the tests
     */
    private static final void checkFile(File file) {
        assertThat("filehandle is null", file, notNullValue());
        assertThat("file does not exist", file.exists(), is(true));
        assertThat("filehandle doesn't point to a file", file.isFile(), is(true));
        assertThat("file size zero", file.length(), not(is(0L)));
        System.out.println("Downloaded File: " + file);
        System.out.println("Size: " + file.length() + " bytes");
    }
}
