package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.codehaus.jackson.map.ObjectMapper;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.service.EnVarsGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class EnvVarsType extends BuildContextCaptureType {

    @DataBoundConstructor
    public EnvVarsType() {
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir) throws BuildContextException {
        ObjectMapper mapper = new ObjectMapper();
        EnVarsGetter enVarsGetter = new EnVarsGetter();
        Map<String, String> jobEnvVars = enVarsGetter.gatherJobEnvVars(build);
        try {
            mapper.writeValue(new File(outputCaptureDir, "envVars.json"), jobEnvVars);
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        }
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