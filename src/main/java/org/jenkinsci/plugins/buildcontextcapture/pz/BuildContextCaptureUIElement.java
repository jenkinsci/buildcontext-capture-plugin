package org.jenkinsci.plugins.buildcontextcapture.pz;

import java.io.File;
import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCaptureUIElement implements Serializable {

    //Local path from the JENKINS_HOME
    private String localPath;

    public BuildContextCaptureUIElement(String localPath) {
        this.localPath = localPath;
    }

    @SuppressWarnings("unused")
    public String getFileName() {
        if (localPath == null) {
            return null;
        }

        if (localPath.indexOf(File.separator) == -1) {
            return localPath;
        }

        if (localPath.endsWith(File.separator)) {
            localPath = localPath.substring(0, localPath.length() - 1);
        }

        int splitIndex = localPath.lastIndexOf(File.separator) + 1;
        if (splitIndex == -1) {
            return null;
        }

        return localPath.substring(localPath.lastIndexOf(File.separator) + 1);
    }

    @SuppressWarnings("unused")
    public String getLocalPath() {
        return localPath;
    }

}
