package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class BuildTimeType extends BuildContextCaptureType {

    @DataBoundConstructor
    public BuildTimeType() {
    }

    @Override
    public void captureAndExport(AbstractBuild build, BuildContextLogger logger, File outputCaptureDir, String format) throws BuildContextException {
        try {
            FileWriter writer = new FileWriter(new File(outputCaptureDir, "buildtime"));
            //Warning: the value is captured at runtime and not totally at the end of the build
            writer.write(String.valueOf(build.getDuration()));
            writer.close();
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        }
    }

    @Override
    public Map<String, ? extends Object> getCapturedElements(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {
        return null;
    }

    @Override
    protected String getFileName() {
        return null;
    }

    @Extension
    @SuppressWarnings("unused")
    public static class BuildTimeTypeDescriptor extends BuildContextCaptureTypeDescriptor<BuildTimeType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
            return BuildTimeType.class;
        }

        @Override
        public String getDisplayName() {
            return "Build time";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }
}
