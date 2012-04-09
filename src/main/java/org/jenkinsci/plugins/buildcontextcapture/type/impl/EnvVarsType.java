package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.service.EnvVarsGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

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
    public Map<String, ? extends Object> getCapturedElements(AbstractBuild build, TaskListener listener) throws BuildContextException {
        EnvVarsGetter enVarsGetter = new EnvVarsGetter();
        return enVarsGetter.gatherJobEnvVars(build);
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