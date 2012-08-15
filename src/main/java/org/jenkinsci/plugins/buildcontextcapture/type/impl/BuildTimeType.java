package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.remoting.VirtualChannel;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.type.FlexibleBuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class BuildTimeType extends FlexibleBuildContextCaptureType {

    @DataBoundConstructor
    public BuildTimeType() {
    }

    @Override
    public void captureAndExport(AbstractBuild build, FilePath outputDir, BuildContextLogger logger) throws BuildContextException {
        try {
            FilePath buildTimeFilePath = getExportedFilePath(outputDir);
            final long duration = build.getDuration();
            buildTimeFilePath.act(new FilePath.FileCallable<Void>() {
                public Void invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                    FileWriter writer = new FileWriter(f);
                    //Warning: the value is captured at runtime and not totally at the end of the build
                    writer.write(String.valueOf(duration));
                    writer.close();
                    return null;
                }
            });
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        } catch (InterruptedException ie) {
            throw new BuildContextException(ie);
        }
    }

    @Override
    public FilePath getExportedFilePath(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {
        return getExportedFilePath(getExportedDir(build));
    }

    private FilePath getExportedFilePath(FilePath outputDir) {
        return outputDir.child("buildtime");
    }

    @Extension
    @SuppressWarnings("unused")
    public static class BuildTimeTypeDescriptor extends BuildContextCaptureTypeDescriptor<BuildTimeType> {

        @Override
        public Class<? extends FlexibleBuildContextCaptureType> getType() {
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
