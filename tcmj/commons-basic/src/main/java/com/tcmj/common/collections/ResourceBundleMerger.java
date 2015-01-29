package com.tcmj.common.collections;

import com.tcmj.common.lang.Check;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>ResourceBundleMerger.</b>
 * <pre>
 * new ResourceBundleMerger(input, output, true, false, false).start();
 * <p>
 * </pre>
 * @author tcmj - Thomas Deutsch
 */
public class ResourceBundleMerger {

    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(ResourceBundleMerger.class);

    private final String[] input;
    private final String output;
    private final boolean sort;
    private final boolean quiet;
    private final boolean delta;
    private final boolean changed = true;//todo show changed keys

    /**
     * Default constructor using all possible options.
     * @param input one or more input files
     * @param output exactly one outpufile
     * @param sort set to true to provide natural ordering
     * @param quiet no output
     */
    private ResourceBundleMerger(String[] input, String output, boolean sort, boolean quiet, boolean delta) {
        this.input = Arrays.copyOf(input, input.length);
        this.output = output;
        this.sort = sort;
        this.quiet = quiet;
        this.delta = delta;
    }

//    /**
//     * close or flush and close readers/writers.
//     * @param toclose
//     */
//    private static final void closeIt(Closeable toclose) {
//
//        try {
//todo            if (Flushable.class.isAssignableFrom(toclose.getClass())) {
//                Flushable writer = (Flushable) toclose;
//                writer.flush();
//            }
//        } catch (Exception e) { /* ignore! */ }
//
//
//        try {
//            toclose.close();
//        } catch (Exception e) { /* ignore! */ }
//
//
//    }
    public final void start() throws Exception {

        if (!quiet) {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
            System.out.println("* * Input: " + Arrays.toString(input));
            System.out.println("* * Output: " + output);
            System.out.println("* * Sort: " + sort);
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        }

        //Create a new Properties object to collect all key/value-pairs
        Properties allprops = new Properties();

        Properties[] inputFiles = new Properties[input.length];

        //Loop through all input files
        for (int i = 0; i < input.length; i++) {

            String inputFileName = input[i];

            //Create a new File handle
            File inputFile = new File(inputFileName);

            //todo use new nio classes
            if (inputFile.isFile()) {

                try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inputFile))) {
                    //add all input properties...
                    inputFiles[i] = new Properties();
                    inputFiles[i].load(reader);
                    allprops.putAll(inputFiles[i]);

//                    Properties single = new Properties();
//                    single.load(reader);
//                    inputFiles[i] = single;
//                    allprops.putAll(single);
                }
            } else {
                if (!quiet) {
                    System.err.println(String.format("Skipping input file: '%s' (not a file or does not exist)", inputFileName));
                }
            }
        }

        if (this.changed) {

            Properties deltafile = new Properties();

            //durchlaufe alle keys ...in allen anderen auch enthalten sind
            for (String key : allprops.stringPropertyNames()) {

                System.err.println("key:" + key);

                //Nutze die eindeutigkeit eines Sets
                Set unique = new HashSet();
                for (int i = 0; i < inputFiles.length; i++) { //ab dem zweiten file..

                    unique.add(inputFiles[i].getProperty(key));

                    StringBuilder bu = new StringBuilder();
                    for (Object value : unique) {
                        if (value != null) {

                            if (bu.length() > 0) {
                                bu.append(";");
                            }
                            bu.append(value);
                        }
                    }
                    deltafile.setProperty(key, bu.toString());

                }

                System.err.println("deltafile:" + deltafile);
//            File outputFile = new File(output);
//            write(deltafile, outputFile);
            }
        }

        if (this.delta) {

            if (input.length != 2) {
                throw new UnsupportedOperationException("Only 2 inputfiles allowed!");
            }

            Set set1 = new HashSet(inputFiles[0].keySet());
            set1.retainAll(inputFiles[1].keySet());
            Set set2 = new HashSet(inputFiles[1].keySet());
            set2.retainAll(inputFiles[0].keySet());
            set1.addAll(set2);
            allprops.keySet().removeAll(set1);

        }
//        else {
        File outputFile = new File(output);
        write(allprops, outputFile);
//        }

        if (!quiet) {
            System.out.println("Successfully finished!");
        }
    }

    /**
     * Renames a file.
     * @param file the file to rename
     * @return a handle to the renamed file
     */
    private File rename(File file) {
        //rename existing files to prevent data-loss
        File fileRenamed = null;
        if (file.isFile()) {
            String renamee = file.getName() + "." + System.currentTimeMillis() + ".bakup";
            fileRenamed = new File(file.getParent(), renamee);
            file.renameTo(fileRenamed);
        }
        return fileRenamed;
    }

    private void write(Properties properties, File outputFile) throws Exception {
        if (!quiet) {
            System.out.println("Start writing to '" + outputFile + "'...");
        }
        File fileRenamed = null;
        try {
            //rename existing files to prevent data-loss
            fileRenamed = rename(outputFile);

            try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                properties.store(writer, "ResourceBundle: " + outputFile.getName());
            }

            if (fileRenamed != null) {
                fileRenamed.delete();
            }

            if (sort) {
                sort(outputFile);
            }

        } catch (Exception e) {
            //rename back
            fileRenamed.renameTo(outputFile);
            throw e;
        }

    }

    /**
     * Reads the already written file again, sorts it and writes it back.
     * @param file a valid file handle
     * @throws IOException
     */
    private void sort(File file) throws IOException {

        if (!quiet) {
            LOG.info("Sorting {}...", file);
            System.out.println("Sorting " + file + "...");

        }

        Set<String> tmpset = new HashSet<String>();
        String thisLine;
        File fileRenamed = null;
        try {
            //read again
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                while ((thisLine = br.readLine()) != null) {
                    tmpset.add(thisLine);
                }
            }

            //sort
            Set<String> sortedSet = new TreeSet<String>(tmpset);

            //rename existing files to prevent data-loss
            fileRenamed = rename(file);

            //write again
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    PrintWriter pw = new PrintWriter(bw)) {
                for (String line : sortedSet) {
                    pw.println(line);
                }
            }

            //delete the previously renamed bakup-file
            if (fileRenamed != null) {
                fileRenamed.delete();
            }

        } catch (IOException e) {
            //rename back
            fileRenamed.renameTo(file);
            throw e;
        }

    }

    /** @return a copy of the input file name array. */
    public String[] getInputFileNames() {
        return Arrays.copyOf(input, input.length);
    }

    /** @return the output file name. */
    public String getOutputFileName() {
        return output;
    }

    /** Sort flag. 
     * @return true if sorting is enabled. */
    public boolean isSort() {
        return sort;
    }

    /**
     * @return the quiet
     */
    public boolean isQuiet() {
        return quiet;
    }

    public static class Builder {

        String[] binput;
        String boutput;
        boolean bsort = false;
        boolean bquiet = false;
        boolean bdelta = false;

        public Builder sort() {
            bsort = true;
            return this;
        }

        public Builder deltaMode() {
            bdelta = true;
            return this;
        }

        public Builder quiet() {
            bquiet = true;
            return this;
        }

        /**
         * Name your input files here. The properties to keep must be set after
         * the one to throw away! The last key overwrites previous keys!
         * @param input the order should be FileWithValueToDrop,FileWithValueToKeep
         * @return Builder pattern object.
         */
        public Builder input(String... input) {
            Check.notNull(input, "No input files set!");
            Check.ensure(!Check.isEmpty(input), "no inputfiles set!");
            Check.ensure(input.length > 1, "need at least 2 inputfiles!");
            binput = input;
            return this;
        }

        /** [Optional] name of the output file. Will be computed if not set! */
        public Builder output(String output) {
            boutput = output;
            return this;
        }

        public ResourceBundleMerger build() {
            Check.notNull(binput, "No input files set!");

            if (boutput == null) {
                boutput = computeOutputFileName();
            }

            Check.notNull(boutput, "No output file set!");
            return new ResourceBundleMerger(binput, boutput, bsort, bquiet, bdelta);
        }

        private String computeOutputFileName() {
            StringBuilder filename = new StringBuilder();
            if (bdelta) {
                filename.append("delta_");
            } else {
                filename.append("merged_");
            }
            for (String file : binput) {
                if (filename.length() > 7) {
                    filename.append("_");
                }
                //append file name without ending
                if (file.lastIndexOf('.') >= 0) {
                    filename.append(file.substring(0, file.lastIndexOf('.')));
                } else {
                    filename.append(file);
                }

            }
            filename.append(".properties");
            return filename.toString();
        }

    }

}
