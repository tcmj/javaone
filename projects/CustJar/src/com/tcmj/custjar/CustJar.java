package com.tcmj.custjar;

import com.tcmj.custjar.mrg.impl.JarFileMerger;
import com.tcmj.custjar.mrg.Merger;
import com.tcmj.custjar.mrg.impl.LightJarFileMerger;
import java.io.File;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * CustJar - Custom Jar Generator
 * @author Thomas Deutsch
 * @contact Thomas-Deutsch [at] tcmj [dot] de (2005 - 2009)
 */
public class CustJar {

    /** Version. */
    public static final String VERSION = "2.00 / 2009";

    /** Creates a new instance of CustJar. */
    public CustJar() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Parameters params = new Parameters();



        try {

            if (args.length < 4) {
                throw new Exception(getUsage());
            }


            Set setLibs = new HashSet();



            for (int i = 0; i < args.length; i++) {
//                System.out.println("o--> "+args[i]);
                String param = args[i];

                if ("-out".equals(param) || "-output".equals(param)) {

                    params.setOutput(args[++i]);

                } else if ("-m".equals(param) || "-manifest".equals(param)) {

                    if (params.getMainclass() != null) {
                        throw new Exception("You cannot use both options 'manifest' and 'mainclass'!");
                    } else {
                        params.setManifest(args[++i]);
                    }

                } else if ("-mc".equals(param) || "-mainclass".equals(param)) {
                    if (params.getManifest() != null) {
                        throw new Exception("You cannot use both options 'manifest' and 'mainclass'!");
                    } else {
                        params.setMainclass(args[++i]);
                    }

                } else if ("-l".equals(param) || "-light".equals(param)) {
                    params.setOnlyneeded(true);

                } else if ("-lib".equals(param) || "-libs".equals(param) || "-libraries".equals(param)) {

                    while (i < args.length - 1) {
                        setLibs.add(args[++i]);
                    }
                    params.setSetInputLibraries(setLibs);
                } else if ("-v".equals(param) || "-verbose".equals(param)) {
                    JarFileMerger.VERBOSE = true;
                } else {
                    throw new Exception(getUsage());
                }


            }


            System.out.println("** * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("** Starting CustJar Version " + VERSION + "              *");
            System.out.println("** Author: Thomas Deutsch (thomas-deutsch@tcmj.de)   *");
            System.out.println("** * * * * * * * * * * * * * * * * * * * * * * * * * *");

            //check availability of parameters:
            if (params.getOutput() == null) {
                throw new Exception("Output file not given!");
            }

            if (params.getManifest() == null && params.getMainclass() == null) {
                throw new Exception("Manifest/MainClass not given!");
            }

            if (setLibs.size() < 1) {
                throw new Exception("No jarfiles found to merge!");
            }


            File fileOutput = new File(params.getOutput());

            System.out.println("** outputfile      : " + fileOutput.getCanonicalPath());

            Merger jfe;
            if (params.isOnlyneeded()) {
                jfe = new JarFileMerger(fileOutput);
                System.out.println("** build light jar : true");
            } else {
                jfe = new LightJarFileMerger(fileOutput);
                System.out.println("** build light jar : false");
            }



            if (params.getManifest() != null) {
                File fileManifest = params.getManifestFile();
                jfe.setManifest(fileManifest);
                System.out.println("** manifest        : " + fileManifest.getCanonicalPath());
            } else {
                jfe.setMainClass(params.getMainclass());
                jfe.putManifestAttribute("Created-By", "CustJar " + VERSION + " by Thomas Deutsch");
                jfe.putManifestAttribute("Built-By", System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getCanonicalHostName());

                System.out.println("** mainclass       : " + params.getMainclass());
            }


            System.out.println("** libs/paths      : " + setLibs.size());
//            System.out.println("** build light jar : "+onlyneeded);
            System.out.println("** verbose         : " + JarFileMerger.VERBOSE);

            System.out.println("** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

            Iterator itLibs = setLibs.iterator();

            while (itLibs.hasNext()) {
                String lib = (String) itLibs.next();
                jfe.addJar(new File(lib));
            }


            jfe.create();

            System.out.println("*** JarFile successfully created! ***");
//            jfe.extract(new File("jdom.jar"),"tempjar/");


        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }

    private static final String getUsage() {
        String LINE = System.getProperty("line.separator");
        StringBuffer s = new StringBuffer(300);
        s.append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        s.append(LINE);
        s.append("** CustJar - Custom Jar - Version " + VERSION);
        s.append(LINE);
        s.append("** Author: Thomas Deutsch (thomas-deutsch@tcmj.de)");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("** Merges some java jarfiles to a single big one!");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("** Usage:");
        s.append(LINE);
        s.append("**\tcustjar [-out outputfile] [-m manifest || -mc mainclass] [-l] [-v] [-lib libraries]");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("** Mandatory Options:");
        s.append(LINE);
        s.append("** \t-out  output\t\tName of the output jarfile to create");
        s.append(LINE);
        s.append("** \t-m    manifest\t\tName of the manifest to be used");
        s.append(LINE);
        s.append("** \t-lib  libraries\t\tList of single jarfiles and/or directories with jarfiles");
        s.append(LINE);
        s.append("** \t\t\t\tto merge (space as separator)");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("** Extended Options:");
        s.append(LINE);
        s.append("** \t-v    verbose\t\tOutputs all entries with state (optional)");
        s.append(LINE);
//        s.append("** \t-l    light\t\tOnly include needed classes (optional)");
//        s.append(LINE);
        s.append("** \t-mc   mainclass\t\tSpecifiy the Main-Class instead of a manifest file");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("** Examples:");
        s.append(LINE);
        s.append("**\tjava -jar CustJar.jar -out out.jar -m manifest.mf -lib lib1.jar lib2.jar lib3.jar");
        s.append(LINE);
        s.append("**\tjava -jar CustJar.jar -out out.jar -mc inteco.MainFrame -lib \"C:\\Project\\dist\\libs\" .\\dist\\abc.jar");
        s.append(LINE);
        s.append("**");
        s.append(LINE);
        s.append("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");
        s.append(LINE);
        return s.toString();
    }
}
