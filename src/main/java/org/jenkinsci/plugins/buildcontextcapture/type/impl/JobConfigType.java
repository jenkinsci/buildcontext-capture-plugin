package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;

/**
 * @author Gregory Boissinot
 */
public class JobConfigType extends BuildContextCaptureType {

    @DataBoundConstructor
    public JobConfigType() {
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir) throws BuildContextException {
        Job job = build.getProject();
        Util.copyFile(job.getConfigFile().getFile(), new File(outputCaptureDir, "config.xml"));
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