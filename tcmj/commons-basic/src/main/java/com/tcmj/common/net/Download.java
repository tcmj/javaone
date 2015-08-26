package com.tcmj.common.net;

import com.tcmj.common.lang.Objects;
import com.tcmj.common.lang.Close;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Download Helper.<br/>
 * With this class you can download files.
 *
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
     * This method uses
     * {@link java.nio.channels.FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)}
     *
     * @param url the url of file which should be downloaded
     * @param target a file handle where you want to save your file. <br/>
     */
    public static void aFile(URL url, File target) throws IOException {
        Objects.notNull(url, "URL parameter may not be null!");
        Objects.notNull(target, "Target file parameter may not be null!");

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
            if (fos != null) {
                Close.inSilence(fos.getChannel());
            }
            Close.inSilence(fos);
            Close.inSilence(rbc);
            Close.inSilence(stream);
        }

    }

    /**
     * Downloads a single file from a given url to the current users temp
     * directory.<br/>
     * This method uses
     * {@link java.nio.channels.FileChannel#transferFrom(java.nio.channels.ReadableByteChannel, long, long)}
     *
     * @param url the url of file which should be downloaded
     * @return a file handle created by
     * {@link File#createTempFile(java.lang.String, java.lang.String)} <br/>
     * additionally uses {@link File#deleteOnExit()} for automatic cleanup
     */
    public static File aFile(URL url) throws IOException {
        File tempfile = File.createTempFile("download", ".tcmj");
        tempfile.deleteOnExit();
        Download.aFile(url, tempfile);
        return tempfile;
    }

}
