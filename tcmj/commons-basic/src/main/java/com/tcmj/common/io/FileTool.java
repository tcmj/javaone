package com.tcmj.common.io;

import com.tcmj.common.lang.Objects;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileTool - Utility class for easier handling of file access.
 * @test com.tcmj.common.io.FileToolTest
 * @author tcmj - Thomas Deutsch
 */
public class FileTool {

    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(FileTool.class);

    /** Tries to locate a given file.
     * order:
     * <ol>
     * <li>absolute locations will be identified first using {@link Path#isAbsolute()}</li>
     * <li>User's home directory via jvm property 'user.home'</li>
     * <li>User's current working directory via jvm property 'user.dir'</li>
     * <li>Relative creating a File object without constructing additional path informations</li>
     * </ol>
     * @param filename to search for
     * @return file handle or null if the file has not been physically found
     */
    public static File locateFile(String filename) {
        int count = 0;
        Path path = FileSystems.getDefault().getPath(filename);
        if (path.isAbsolute()) {
            if (FileTool.checkIsFileAndExists(path)) {
                return path.toFile();
            } else {
                return null;
            }
        }

        LOG.trace("{}. Try to locate '{}' in 'user.home' directory...", ++count, filename);
        File userHomeFile = FileSystems.getDefault().getPath(System.getProperty("user.home"), filename).toFile();
        if (checkIsFileAndExists(userHomeFile)) {
            return userHomeFile;
        }

        LOG.trace("{}. Try to locate '{}' in current working directory...", ++count, filename);
        File workingDirFile = FileSystems.getDefault().getPath(System.getProperty("user.dir"), filename).toFile();
        if (checkIsFileAndExists(workingDirFile)) {
            return workingDirFile;
        }

        LOG.trace("{}. find file '{}' without constructing a path...", ++count, filename);
        File filetofind = new File(filename);
        if (checkIsFileAndExists(filetofind)) {
            return filetofind;
        }
        return null;
    }

    /** searches the given file.
     * in following order:
     * <ol>
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

        File filetofind = locateFile(filename);
        if (filetofind != null) {
            return filetofind;
        }

        Path path = FileSystems.getDefault().getPath(filename);

        int count = path.isAbsolute() ? 0 : 3;

        //try to find the application path manually
        LOG.trace("{}. find file '{}' via ProtectionDomain-CodeSource-Location...", ++count, filename);
        URL url = parentClassJar.getProtectionDomain().getCodeSource().getLocation();
        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
            File parentfilePath = new File(decodedPath);
            File filetofind2 = new File(parentfilePath.getParent(), filename);
            if (checkIsFileAndExists(filetofind2)) {
                return filetofind2;
            }
        } catch (Exception ex) {
            LOG.debug(ex.getMessage());
        }

        //try to find the application path manually one level higher
        LOG.trace("{}. try to locate '{}' manually one level higher...", ++count, filename);

        try {
            File filetofindhigher1 = new File(path.toAbsolutePath().getParent().getParent().toFile(), filename);
            if (checkIsFileAndExists(filetofindhigher1)) {
                return filetofindhigher1;
            }
        } catch (Exception ex) {
            LOG.debug("Exception locating the file in parent directory: {}", ex.getMessage());
        }

        //TODO load stream out of the jar
        return null;
    }

    /**
     * see {@link FileTool#checkIsFileAndExists(Path)}
     */
    public static boolean checkIsFileAndExists(File file) {
        return checkIsFileAndExists(file.toPath());
    }

    /**
     * Checks if a file is not a directory and physically exists in the file system.
     * @param file Java 7 path object (NullPointerException if null)
     * @return true if file is a regular file and not a directory
     */
    public static boolean checkIsFileAndExists(Path file) {
        Objects.notNull(file, "Cannot perform file check on a null reference!");
        if (Files.isRegularFile(file)) {
            LOG.debug("Successfully found: '{}'!", file.toAbsolutePath());
            return true;
        } else {
            if (Files.isDirectory(file)) {
                LOG.trace("Given parameter is a directory: '{}'!", file.toAbsolutePath());
            } else {
                LOG.trace("File does not exist: '{}'!", file.toAbsolutePath());
            }
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
        LOG.debug("Reading file {}...", file);
        String theString = null;
        try (Scanner scanner = new Scanner(FileTool.class.getResourceAsStream(file), StandardCharsets.UTF_8.name()).useDelimiter("\\Z")) {  //'\Z' means EOF
            theString = scanner.hasNext() ? scanner.next() : "";
        } finally {
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
            LOG.debug("Reading all bytes of file '{}'...{}", file, charset);
            String content;
            Charset cset;
            if (charset == null || charset.length == 0 || charset[0] == null) {
                cset = StandardCharsets.UTF_8;
            } else {
                cset = Charset.forName(charset[0]);
            }
            content = new String(Files.readAllBytes(file), cset);
            LOG.trace("Content of file {}: {}", file, content.length());
            return content;
        } catch (IOException ex) {
            LOG.error("Error reading all bytes from {}", file, ex);
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

    /**
     * Try to delete a file catching all exceptions.
     * @return true only on a successfull deletion otherwise false
     */
    public static boolean delete(File file) {
        try {
            if(file!=null) {
                return file.delete();
            }
        } catch (Exception ex) {
            LOG.trace("Delete on file '{}' results in: {}!", file, ex.getMessage());
        }
        return false;
    }


    /**
     * Very fast, native way to copy a file.
     * @param inputFile source file which has to exist
     * @param outputFile destination file which must be a non null file reference. Overwrites the destination unasked!
     */
    public static void copy(File inputFile, File outputFile) {
        Objects.notNull(inputFile, "Parameter 'inputFile' (1) cannot be null!");
        Objects.notNull(outputFile, "Parameter 'outputFile' (2) cannot be null!");
        Objects.ensure(checkIsFileAndExists(inputFile), "Cannot perform copy because source file does not exist!");
        try (FileChannel in = new FileInputStream(inputFile).getChannel();
             FileChannel out = new FileOutputStream(outputFile).getChannel()) {
            in.transferTo(0, in.size(), out); //native I/O operation
            LOG.debug("Successfully created file '{}'!", outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
