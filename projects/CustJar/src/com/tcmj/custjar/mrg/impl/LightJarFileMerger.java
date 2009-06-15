/*
 * JarFileMerger.java
 * Created on 14. Juli 2005, 23:05
 */
package com.tcmj.custjar.mrg.impl;

import com.tcmj.custjar.exc.CustJARException;
import com.tcmj.custjar.mrg.Merger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

/**
 * Extracts a specific Jarfile to a folder.
 * @author Thomas Deutsch
 * @contact Thomas-Deutsch [at] tcmj [dot] de (2005 - 2009)
 */
public class LightJarFileMerger implements Merger {

    /** Jar File to be created as Output */
    protected File fileOutputJar;
    /** Regular Expression to exclude all 'META-INF' Entries */
    private static final Pattern p = Pattern.compile("^(META-INF/){1}.*");
    /** Manifest used to create the Output Jar */
    protected Manifest manifest;
    /** MainClass to create a Manifest in the Output Jar */
    protected String mainclass;
    /** Array to hold all Input-Jars */
    protected Set setJars2Include;
    public static boolean VERBOSE = false;
//    private JarFile jarfile;
    private static final int EOF = -1;

    /**
     * Creates a new instance of JarFileMerger
     */
    public LightJarFileMerger(File outjarfile)
            throws CustJARException, IOException {
        if (outjarfile == null) {
            throw new CustJARException("OutputJarFile Parameter cannot be null!");
        }
        if (outjarfile.isDirectory()) {
            throw new CustJARException("JarFile Parameter must not point to a Directory - Just to a File!");
        }

        this.fileOutputJar = outjarfile;

        //create new set to hold the input jars:
        setJars2Include = new LinkedHashSet();



    }

    /** Adds one or more Jarfiles to this container.
     * depending on the File parameter there will be one jar file added if the
     * parameter is a file or all jar files of the whole directory if the
     * parameter is a directory
     * @param jar Jarfile or directory to include into the Output Jar.
     */
    public void addJar(File jar) throws CustJARException {
        if (!jar.exists()) {

            throw new CustJARException("File/Directory '" + jar.getPath() + "' not found on harddisk!");

        } else if (jar.isDirectory()) {

            FileFilter filter = new FileFilter() {

                public boolean accept(File pathname) {

                    String extension = pathname.getName();

                    int dotpos = extension.lastIndexOf(".");

                    if (dotpos != -1) {
                        extension = extension.substring(dotpos + 1);
                        if ("jar".equals(extension.toLowerCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            };


            File[] arFile = jar.listFiles(filter);

            for (int i = 0; i < arFile.length; i++) {

//                System.out.println("++ adding jar from directory: " + arFile[i].getPath());

                setJars2Include.add(arFile[i]);

            }

        } else {
//            try {
//                System.out.println("++ adding jar from file:      " + jar.getCanonicalPath());
//            } catch (IOException ex) {
//                System.out.println("++ adding jar from file:      " + jar.getAbsolutePath());
//            }
            setJars2Include.add(jar);

        }

    }

    /** Setter for the Manifest to be used for Output.
     */
    public void setManifest(File file) throws CustJARException, IOException {
        if (file == null || !file.exists() || file.isDirectory()) {
            throw new CustJARException("File Handle must point to a existing Manifest!");
        }
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(file));
        this.manifest = new Manifest(bis);

//        java.util.jar.
    }

    public void setMainClass(String mainclass) throws CustJARException {

        this.mainclass = mainclass;

        //create a new manifest
        this.manifest = new Manifest();
        putManifestAttribute("Manifest-Version", "1.0");
        putManifestAttribute("Main-Class", mainclass);

    }

    public void putManifestAttribute(String key, String value) {
        if (this.manifest != null) {
            this.manifest.getMainAttributes().putValue(key, value);
        }
    }

    public void create() throws CustJARException, FileNotFoundException, IOException {

        int count_included = 0, count_excluded = 0, count_duplicates = 0, count_all = 0;


        if (this.setJars2Include.isEmpty()) {
            throw new CustJARException("No Jarfiles were included! Use 'addJar(File)'!");
        }

        Set setNoDuplicates = new HashSet();

        JarOutputStream tempJar =
                new JarOutputStream(new FileOutputStream(this.fileOutputJar), this.manifest);

        Iterator itInJars = setJars2Include.iterator();

        while (itInJars.hasNext()) {

            File actfile = (File) itInJars.next();


            if (!VERBOSE) {
                System.out.println("++ " + actfile.getName());
            } else {
                System.out.println("++ " + actfile);
            }


            JarFile actjar = new JarFile(actfile);


            // Allocate a buffer for reading entry data.
            byte[] buffer = new byte[1024];
            int bytesRead;

            for (Enumeration entries = actjar.entries(); entries.hasMoreElements();) {

                // Get the next entry.
                JarEntry entry = (JarEntry) entries.nextElement();

                // Increment the 'all' counter
                count_all++;

                // Name of the entry
                String input = entry.getName();

                // Look out there has to be no duplicates

                // If the actual element is already included..
                if (setNoDuplicates.contains(input)) {

//                    //don't count manifest entries and domain level dirs:
//                    if ("META-INF/".equalsIgnoreCase(input) ||
//                            "META-INF/MANIFEST.MF".equalsIgnoreCase(input) ||
//                            "com/".equalsIgnoreCase(input) ||
//                            "org/".equalsIgnoreCase(input) ||
//                            "de/".equalsIgnoreCase(input)) {
//                        /* do not do anything at the moment */
//                    } else {

                    // Increment the duplicate counter and log
                    // but only for .class files
                    if (input.endsWith(".class")) {
                        count_duplicates++;

//                            if (VERBOSE) {
                        log("\tDuplicate: " + input);
//                            }
//                        }
                    }else{
                        count_included++;
                    }

                } else {

                    //if the act elem is not in the 'META-INF' directory....include!
                    if (p.matcher(input).matches()) {

                        //check again to exclude only manifest and *.sf files
                        String lowercase = input.toLowerCase();
                        boolean exclude = false;
                        if (lowercase.indexOf("manifest.mf") > 0) {
                            exclude = true;
                        }
                        if (lowercase.indexOf(".sf") > 0) {
                            exclude = true;
                        }


                        // Increment the exclude counter and log
                        if (exclude) {
                            count_excluded++;
                            if (VERBOSE) {
                                log("\tExcluding: " + input);
                            }
                        } else {
                            // Increment the included counter and log
                            count_included++;
                            setNoDuplicates.add(input);
                            if (VERBOSE) {
                                log("Including META: " + input);
                            }
                            // Get an input stream for the entry.
                            InputStream entryStream = actjar.getInputStream(entry);
                            tempJar.putNextEntry(new JarEntry(entry.getName()));
                            while ((bytesRead = entryStream.read(buffer)) != EOF) {
                                tempJar.write(buffer, 0, bytesRead);
                            }

                            entryStream.close();
                        }


                    } else {
count_included++;
                        // Increment the included counter and log
                        if (input.endsWith(".class")) {
                            
                            if (VERBOSE) {

                                log("\tIncluding: " + input);
                            }
                        }
                        // Get an input stream for the entry.
                        InputStream entryStream = actjar.getInputStream(entry);


//                    //AUTOJAR...
//                    try{
//                        ClassParser  cparser = new ClassParser(entryStream,input);
//                        JavaClass klass = cparser.parse();
//
//                        System.out.println("A: "+java.util.Arrays.asList(klass.getFields()));
//                    }catch(Exception ex){
//
//                    }
                        //..AUTOJAR



                        // Read the entry and write it to the temp jar.
                        //tempJar.putNextEntry(entry);
                        setNoDuplicates.add(input);
                        tempJar.putNextEntry(new JarEntry(entry.getName()));

                        while ((bytesRead = entryStream.read(buffer)) != EOF) {
                            tempJar.write(buffer, 0, bytesRead);
                        }

                        entryStream.close();
                    }
                }
            }
        }

        System.out.println();
        System.out.println("** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        //Overal info about inclusion
        log("** Included: " + count_included + " Excluded: " + count_excluded +
                " Duplicates: " + count_duplicates + " All Entries: " + count_all +
                " JarFiles: " + setJars2Include.size());
        System.out.println("** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        tempJar.close();

    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    public static void extract(File jar, String directory)
            throws ZipException, IOException {

        JarFile actjar = new JarFile(jar);


        for (Enumeration entries = actjar.entries(); entries.hasMoreElements();) {

            // Get the next entry.
            JarEntry entry = (JarEntry) entries.nextElement();
            String ename = entry.getName();

            File file = new File(directory + ename);

            if (entry.isDirectory()) {
                log("Extracting Folder: " + ename);
                file.mkdirs();


            } else {
                log("Extracting File  : " + ename);
                // Get an input stream for the entry.
                InputStream entryStream = actjar.getInputStream(entry);

                BufferedInputStream bis = new BufferedInputStream(entryStream);

                File dir = new File(file.getParent());

                dir.mkdirs();

                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (int c; (c = bis.read()) != EOF;) // oder schneller
                {
                    bos.write((byte) c);
                }

                bos.close();
                fos.close();
            }
        }
    }

    public static void extract(JarFile actjar, JarEntry entry)
            throws ZipException, IOException {

        File file = new File(entry.getName());

        if (entry.isDirectory()) {
            file.mkdirs();
        } else {

            // Get an input stream for the entry.
            InputStream entryStream = actjar.getInputStream(entry);

            BufferedInputStream bis = new BufferedInputStream(entryStream);

            File dir = new File(file.getParent());

            dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int c; (c = bis.read()) != EOF;) // oder schneller
            {
                bos.write((byte) c);
            }

            bos.close();
            fos.close();
        }
    }

    public void extractJar() {
    }
}//end: class
