package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;

/**
 * @author Gregory Boissinot
 */
public abstract class FlexibleBuildContextCaptureType extends BuildContextCaptureType implements ExtensionPoint, Describable<FlexibleBuildContextCaptureType> {

    public Descriptor<FlexibleBuildContextCaptureType> getDescriptor() {
        return (FlexibleBuildContextCaptureTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    @Override
    public void capture(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {
        captureAndExport(build, getExportedDir(build), logger);
    }

    public abstract void captureAndExport(AbstractBuild build, FilePath outputDir, BuildContextLogger logger) throws BuildContextException;

}
