package com.tcmj.common.lang;

import com.tcmj.common.text.HumanReadable;
import org.apache.commons.lang3.StringUtils;

/**
 * Common application tools.
 *
 * @author tcmj - Thomas Deutsch
 * @since 23.01.2011
 */
public class Application {

    private static volatile Application instance;
    final Class<?> context;

    /** private default no-arg-constructor. */
    private Application(final Class<?> context) {
        this.context = context;
    }

    public static Application get(final Class<?> context) {
        Application result = instance;
        if (result == null) { // First check (no locking)
            synchronized (Application.class) {
                result = instance;
                if (result == null) { // Second check (with locking)
                    instance = result = new Application(context);
                }
            }
        }
        return result;
    }

    /**
     * Tries to read the application title provided from the manifests implementation entries. The following order will be used to get the title
     * <ol>
     * <li>Manifest entry 'Implementation-Title'</li>
     * <li>Manifest entry 'Specification-Title'</li>
     * </ol>
     *
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the title or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public String getApplicationTitle(Class<?> context) {
        Objects.notNull(context, "Parameter 'context' may not be null!");
        String title = "";
        String implementationTitle = context.getPackage().getImplementationTitle();
        if (StringUtils.isNotBlank(implementationTitle)) {
            title = implementationTitle;
        } else {
            String specificationTitle = context.getPackage().getSpecificationTitle();
            if (StringUtils.isNotBlank(specificationTitle)) {
                title = specificationTitle;
            }
        }
        return title;
    }

    public String getApplicationTitle() {
        return getApplicationTitle(context);
    }

    public String getApplicationVendor() {
        return getApplicationVendor(context);
    }

    public String getApplicationVersion() {
        return getApplicationVersion(context);
    }

    /**
     * Tries to read the application version provided from the manifests implementation entries. The following order will be used to get it
     * <ol>
     * <li>Manifest entry 'Implementation-Version'</li>
     * <li>Manifest entry 'Specification-Version'</li>
     * </ol>
     *
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the version or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public String getApplicationVersion(Class<?> context) {
        Objects.notNull(context, "Parameter 'context' may not be null!");
        String version = "";
        String implementationVersion = context.getPackage().getImplementationVersion();
        if (StringUtils.isNotBlank(implementationVersion)) {
            version = implementationVersion;
        } else {
            String specificationVersion = context.getPackage().getSpecificationVersion();
            if (StringUtils.isNotBlank(specificationVersion)) {
                version = specificationVersion;
            }
        }
        return version;
    }

    /**
     * Tries to read the application vendor provided from the manifests implementation entries. The following order will be used to get the vendor
     * <ol>
     * <li>Manifest entry 'Implementation-Vendor'</li>
     * <li>Manifest entry 'Specification-Vendor'</li>
     * </ol>
     *
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the vendor or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public String getApplicationVendor(Class<?> context) {
        Objects.notNull(context, "Parameter 'context' may not be null!");
        String title = "";
        String implementationVendor = context.getPackage().getImplementationVendor();
        if (StringUtils.isNotBlank(implementationVendor)) {
            title = implementationVendor;
        } else {
            String specificationVendor = context.getPackage().getSpecificationVendor();
            if (StringUtils.isNotBlank(specificationVendor)) {
                title = specificationVendor;
            }
        }
        return title;
    }

    /**
     * @return 'Oracle Corporation Java 1.8.0_25 (64bit/amd64)'
     */
    public String getJavaVersionString() {
        return System.getProperty("java.vm.vendor") + //Oracle Corporation
                " Java " + System.getProperty("java.version") + //1.8.0_25
                " (" + System.getProperty("sun.arch.data.model") + "bit/" + System.getProperty("os.arch") + ")";  // (64bit/amd64)
    }

    /**
     * @return
     */
    public String getJavaVmName() {
        return System.getProperty("java.vm.name"); //Java HotSpot(TM) 64-Bit Server VM
    }

    /**
     * @return 'Windows 7 (6.1)'
     */
    public String getOsName() {
        return System.getProperty("os.name") + " ("
                + System.getProperty("os.version") + ")"; //Windows 7 (6.1)
    }

    /**
     * @return 'FileEncoding: UTF-8 UserCountry: DE'
     */
    public String getJavaEncodingInfos() {
        return "FileEncoding: " + System.getProperty("file.encoding") + //UTF-8
                " UserCountry: " + System.getProperty("user.country");
    }

    /**
     * @return 'Timezone: Europe/Berlin'
     */
    public String getJavaTimezone() {
        return "Timezone: " + System.getProperty("user.timezone");  //Europe/Berlin
    }

    public Float getJavaClassVersion() {
        try {
            return Float.valueOf(System.getProperty("java.class.version"));
        } catch (Exception e) {
            return null;
        }
    }
    public String getMaxMemory() {
        return HumanReadable.bytes(Runtime.getRuntime().maxMemory());
    }

}
