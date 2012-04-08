package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.service.EnVarsGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class EnvVarsType extends BuildContextCaptureType {

    @DataBoundConstructor
    public EnvVarsType() {
    }

    @Override
    protected String getFileName() {
        return "envVars";
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir, String format) throws BuildContextException {
        EnVarsGetter enVarsGetter = new EnVarsGetter();
        Map<String, String> jobEnvVars = enVarsGetter.gatherJobEnvVars(build);
        export(jobEnvVars, outputCaptureDir, format);
    }

    @Extension
    @SuppressWarnings("unused")
    public static class EnvVarsTypeDescriptor extends BuildContextCaptureTypeDescriptor<EnvVarsType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
            return EnvVarsType.class;
        }

        @Override
        public String getDisplayName() {
            return "Environment variables";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }
}