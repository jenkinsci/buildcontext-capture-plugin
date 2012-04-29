package org.jenkinsci.plugins.buildcontextcapture;

import hudson.model.Action;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCaptureAction implements Action {

    private String exportedFormat;

    public BuildContextCaptureAction(String exportedFormat) {
        this.exportedFormat = exportedFormat;
    }

    public String getExportedFormat() {
        return exportedFormat;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }
}
