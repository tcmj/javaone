package com.tcmj.custjar;

import java.io.File;
import java.util.Set;

/**
 * CustJar - Custom Jar Generator.
 * @author Thomas Deutsch
 * @contact Thomas-Deutsch [at] tcmj [dot] de (2005 - 2009)
 */
public class Parameters {

    private String output;
    private String manifest;
    private String mainclass;
    private boolean onlyneeded;

    private Set setInputLibraries;

    public Parameters() {
    }

    

    public Parameters(String output, String manifest, String mainclass, boolean onlyneeded) {
        this.output = output;
        this.manifest = manifest;
        this.mainclass = mainclass;
        this.onlyneeded = onlyneeded;
    }


    


    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return the manifest
     */
    public String getManifest() {
        return manifest;
    }
/**
     * @return the Manifest File handle
     */
    public File getManifestFile() {
        return new File(getManifest());
    }
    /**
     * @param manifest the manifest to set
     */
    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    /**
     * @return the mainclass
     */
    public String getMainclass() {
        return mainclass;
    }

    /**
     * @param mainclass the mainclass to set
     */
    public void setMainclass(String mainclass) {
        this.mainclass = mainclass;
    }

    /**
     * @return the onlyneeded
     */
    public boolean isOnlyneeded() {
        return onlyneeded;
    }

    /**
     * @param onlyneeded the onlyneeded to set
     */
    public void setOnlyneeded(boolean onlyneeded) {
        this.onlyneeded = onlyneeded;
    }

    /**
     * @return the setInputLibraries
     */
    public Set getSetInputLibraries() {
        return setInputLibraries;
    }

    /**
     * @param setInputLibraries the setInputLibraries to set
     */
    public void setSetInputLibraries(Set setInputLibraries) {
        this.setInputLibraries = setInputLibraries;
    }
}
