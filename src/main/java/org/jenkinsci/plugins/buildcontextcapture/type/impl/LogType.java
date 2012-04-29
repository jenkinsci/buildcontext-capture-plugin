package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.remoting.VirtualChannel;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.type.FlexibleBuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.FlexibleBuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class LogType extends FlexibleBuildContextCaptureType {

    @DataBoundConstructor
    public LogType() {
    }

    @Override
    public void captureAndExport(AbstractBuild build, FilePath outputDir, BuildContextLogger logger) throws BuildContextException {

        final File logFile = build.getLogFile();
        try {
            outputDir.act(new FilePath.FileCallable<Void>() {
                public Void invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                    Util.copyFile(logFile, new File(f, "log"));
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
        return getExportedDir(build).child("log");
    }

    @Extension
    @SuppressWarnings("unused")
    public static class LogTypeDescriptor extends FlexibleBuildContextCaptureTypeDescriptor<LogType> {

        @Override
        public Class<? extends FlexibleBuildContextCaptureType> getType() {
            return LogType.class;
        }

        @Override
        public String getDisplayName() {
            return "Log";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }
}
