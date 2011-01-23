/* 
 *  Copyright (C) 2011 Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.lang;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Common application tools.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 23.01.2011
 */
public class Application {

    /**
     * default no-arg-constructor.
     */
    private Application() {
    }


    /** Tries to read the application title provided from the manifests implementation entries.
     * The following order will be used to get the title
     * <ol>
     * <li>Manifest entry 'Implementation-Title</li>
     * <li>Manifest entry 'Specification-Title</li>
     * </ol>
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the title or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public static String getApplicationTitle(Class context) {
        Validate.notNull(context, "Parameter 'context' may not be null!");
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

    /** Tries to read the application version provided from the manifests implementation entries.
     * The following order will be used to get it
     * <ol>
     * <li>Manifest entry 'Implementation-Version</li>
     * <li>Manifest entry 'Specification-Version</li>
     * </ol>
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the version or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public static String getApplicationVersion(Class context) {
        Validate.notNull(context, "Parameter 'context' may not be null!");
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

    /** Tries to read the application vendor provided from the manifests implementation entries.
     * The following order will be used to get the vendor
     * <ol>
     * <li>Manifest entry 'Implementation-Vendor</li>
     * <li>Manifest entry 'Specification-Vendor</li>
     * </ol>
     * @param context a class which is located in the same jarfile of the manifest.mf
     * @return the vendor or an empty string
     * @throws java.lang.IllegalArgumentException if the parameter 'context' is null
     */
    public static String getApplicationVendor(Class context) {
        Validate.notNull(context, "Parameter 'context' may not be null!");
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


}
