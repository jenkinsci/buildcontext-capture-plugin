package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Job;
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
public class JobConfigType extends BuildContextCaptureType {

    @DataBoundConstructor
    public JobConfigType() {
    }

    @Override
    public void captureAndExport(AbstractBuild build, TaskListener listener, File outputCaptureDir, String format) throws BuildContextException {
        Job job = build.getProject();
        Util.copyFile(job.getConfigFile().getFile(), new File(outputCaptureDir, "config.xml"));
    }

    @Override
    public Map<String, ? extends Object> getCapturedElements(AbstractBuild build, TaskListener listener) throws BuildContextException {
        return null;
    }

    @Override
    protected String getFileName() {
        return null;
    }

    @Extension
    @SuppressWarnings("unused")
    public static class JobConfigTypeDescriptor extends BuildContextCaptureTypeDescriptor<JobConfigType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
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