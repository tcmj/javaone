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

import com.tcmj.common.tools.lang.Check;
import com.tcmj.common.tools.lang.Close;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Download Helper.<br/>
 * With this class you can download files.
 * @author tcmj - Thomas Deutsch
 * @since 01.05.2012
 */
class Download {

    /**
     * instantiation not allowed!
     */
    private Download() {
    }
    
    /**
     * Downloads a single file from a given url.<br/>
     * This method uses {@link FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)}
     * @param url the url of file which should be downloaded
     * @param target a file handle where you want to save your file. <br/>
     */
    public static void aFile(URL url, File target) throws IOException {
        Check.notNull(url, "URL parameter may not be null!");
        Check.notNull(target, "Target file parameter may not be null!");
        
        InputStream stream = null;
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        
        try {
            stream = url.openStream();
            rbc = Channels.newChannel(stream);
            fos = new FileOutputStream(target);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        } catch (IOException e) {
            throw e;
        } finally { //cleanup:
            Close.quiet(fos.getChannel());
            Close.quiet(fos);
            Close.quiet(rbc);
            Close.quiet(stream);
        }
        
    }
    
    
    /**
     * Downloads a single file from a given url to the current users temp directory.<br/>
     * This method uses {@link FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)}
     * @param url the url of file which should be downloaded
     * @return a file handle created by {@link File#createTempFile(java.lang.String, java.lang.String)} <br/>
     *         additionally uses {@link File#deleteOnExit()} for automatic cleanup
     */
    public static File aFile(URL url) throws IOException {
        File tempfile = File.createTempFile("download", ".tcmj");
        tempfile.deleteOnExit();
        Download.aFile(url, tempfile);
        return tempfile;
    }
    
    
}
