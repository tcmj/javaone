package com.tcmj.common.io;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileTool.
 * @author tcmj
 */
public class FileTool {

    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(FileTool.class);

    
    /** searches the given file.
     * in following order:
     * <ol>
     * <li>A specific jvm property namely 'app.files'</li>
     * <li>User's home directory via jvm property 'user.home'</li>
     * <li>User's current working directory via jvm property 'user.dir'</li>
     * <li>Relative creating a File object without path informations</li>
     * <li>parentClassJar.getProtectionDomain().getCodeSource().getLocation()</li>
     * <li>special case for the service wrapper installation (/conf directory)</li>
     * <li>User's home directory via jvm property 'user.home'</li>
     * <li>parent of parentClassJar.getProtectionDomain().getCodeSource().getLocation()</li>
     * </ol>
     * @param filename to search for
     * @param parentClassJar any class where to start the search
     * @return file handle or null if not found
     */
    public static File locateFile(String filename, Class parentClassJar) {
        int count = 0;
        
        LOG.trace("{}. Try to locate '{}' via jvm property: 'app.files'...", ++count, filename);
        String appFileDir = System.getProperty("app.files");
        if (appFileDir != null) {
            File appFilesFile = new File(appFileDir, filename);
            if (checkFileExists(appFilesFile)) {
                return appFilesFile;
            }
        }else{
            LOG.trace("JVM property 'app.files' not defined! (Pattern: -Dapp.files=File)");
        }

        LOG.trace("{}. Try to locate '{}' in 'user.home' directory...", ++count, filename);
        String userDir = System.getProperty("user.home");
        if (userDir != null) {
            File userHomeFile = new File(userDir, filename);
            if (checkFileExists(userHomeFile)) {
                return userHomeFile;
            }
        }
        
        LOG.trace("{}. Try to locate '{}' in current working directory...", ++count, filename);
        String workingDir = System.getProperty("user.dir");
        if (workingDir != null) {
            File workingDirFile = new File(workingDir, filename);
            if (checkFileExists(workingDirFile)) {
                return workingDirFile;
            }
        }

        LOG.trace("{}. find file '{}' relative without path (should be classpath)...", ++count, filename);
        File filetofind = new File(filename);
        if (checkFileExists(filetofind)) {
            return filetofind;
        }

        //try to find the application path manually
        LOG.trace("{}. find file '{}' via ProtectionDomain-CodeSource-Location...", ++count, filename);
        URL url = parentClassJar.getProtectionDomain().getCodeSource().getLocation();
        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
            File parentfilePath = new File(decodedPath);
            File filetofind2 = new File(parentfilePath.getParent(), filename);
            if (checkFileExists(filetofind2)) {
                return filetofind2;
            }
        } catch (UnsupportedEncodingException ex) {
            LOG.debug(ex.getMessage());
        }

        //special case for the service wrapper installation (/conf directory)
        LOG.trace("{}. find file '{}' relative in a conf directory...", ++count, filename);
        File fileInServiceConf = new File("conf", filename);
        if (checkFileExists(fileInServiceConf)) {
            return fileInServiceConf;
        }

        //try to find the application path manually one level higher
        LOG.trace("{}. find file '{}' try to find the path manually one level higher...", ++count, filename);

        URL url2 = parentClassJar.getProtectionDomain().getCodeSource().getLocation();
        File filetofindhigher1 = new File(url2.getPath()).getParentFile();
        String urlPath;
        try {
            urlPath = URLDecoder.decode(filetofindhigher1.getParent(), "UTF-8");
            File filetofindhigher2 = new File(urlPath, filename);
            if (checkFileExists(filetofindhigher2)) {
                return filetofindhigher2;
            }
        } catch (UnsupportedEncodingException ex) {
            LOG.debug(ex.getMessage());
        }
        
        
        //TODO load stream out of the jar
        
        return null;
    }
    
    private static boolean checkFileExists(File file) {
        if (file.exists()) {
            LOG.debug("{} successfully found in '{}'!", file.getName(), file.getAbsolutePath());
            return true;
        } else {
            LOG.trace("File does not exist: '{}'!", file.getAbsolutePath());
            return false;
        }
    }

    /**
     * Reads a whole file into a String.<br>
     * Best for reading files within jarfiles.<br>
     * Uses a Scanner with EOF-delimiter and UTF-8 charset.
     * @param file uri passed to {@link Class#getResourceAsStream(java.lang.String)}
     * @return file content as String or null on any exceptions.
     */
    public static final String read(String file) {
        LOG.info("reading file {}...", file);
        try (Scanner scanner = new Scanner( FileTool.class.getResourceAsStream(file), StandardCharsets.UTF_8.name()).useDelimiter("\\Z")) {  //'\Z' means EOF
            String theString = scanner.hasNext() ? scanner.next() : "";
            LOG.debug("content of file {}: {}", file, theString);
            return theString;
        }
    }

    /**
     * Reads a whole file into a String.
     * Any IOExceptions will be logged as errors (slf4j)
     * @param file path e.g. {@link Paths#get(java.lang.String, java.lang.String...) }
     * @param charset optional possibility to set a specific charset. Defaults to UTF-8
     * @return file content as String or null on any exceptions
     */
    public static final String read(Path file, String... charset) {
        try {
            LOG.debug("reading file {} to string...", file);
            String content;
            if (charset == null || charset.length == 0 || charset[0] == null) {
                content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
            } else {
                content = new String(Files.readAllBytes(file), charset[0]);
            }
            LOG.trace("content of file {}: {}", file, content);
            return content;
        } catch (IOException ex) {
            LOG.error("error reading all bytes from {}", file, ex);
            return null;
        }
    }

    /**
     * Iterates through a file line by line and executes a lambda action.
     * Example:
     * action = s -> System.out.println(s);
     * @param path location of thfile
     * @param action s -> System.out.println(s)
     */
    public static final void forEachLine(Path path, Consumer<? super String> action) {
        try {
            Stream<String> lines = Files.lines(path);
            lines.forEach(action);
        } catch (IOException ex) {
            LOG.error("Error executing action {} on file {}!", action, path, ex);
        }
    }

}
