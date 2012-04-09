package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class LogType extends BuildContextCaptureType {

    @DataBoundConstructor
    public LogType() {
    }

    @Override
    protected String getFileName() {
        return null;
    }

    @Override
    public void captureAndExport(AbstractBuild build, TaskListener listener, File outputCaptureDir, String format) throws BuildContextException {
        Util.copyFile(build.getLogFile(), new File(outputCaptureDir, "log"));
    }

    @Override
    public Map<String, ? extends Object> getCapturedElements(AbstractBuild build, TaskListener listener) throws BuildContextException {
        return null;
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
