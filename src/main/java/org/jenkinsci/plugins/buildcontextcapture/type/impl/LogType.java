package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;

/**
 * @author Gregory Boissinot
 */
public class LogType extends BuildContextCaptureType {

    @DataBoundConstructor
    public LogType() {
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir) throws BuildContextException {
        Util.copyFile(build.getLogFile(), new File(outputCaptureDir, "log"));
    }

    @Extension
    @SuppressWarnings("unused")
    public static class LogTypeDescriptor extends BuildContextCaptureTypeDescriptor<LogType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
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
