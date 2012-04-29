package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Job;
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
public class JobConfigType extends FlexibleBuildContextCaptureType {

    @DataBoundConstructor
    public JobConfigType() {
    }

    @Override
    public void captureAndExport(AbstractBuild build, FilePath outputDir, BuildContextLogger logger) throws BuildContextException {
        Job job = build.getProject();
        final File configFile = job.getConfigFile().getFile();
        try {
            outputDir.act(new FilePath.FileCallable<Void>() {
                public Void invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
                    Util.copyFile(configFile, new File(f, "config.xml"));
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
        return getExportedDir(build).child("config.xml");
    }


    @Extension
    @SuppressWarnings("unused")
    public static class JobConfigTypeDescriptor extends FlexibleBuildContextCaptureTypeDescriptor<JobConfigType> {

        @Override
        public Class<? extends FlexibleBuildContextCaptureType> getType() {
            return JobConfigType.class;
        }

        @Override
        public String getDisplayName() {
            return "Job configuration file";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }
}