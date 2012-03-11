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
public class JobInfraType extends BuildContextCaptureType {

    @DataBoundConstructor
    public JobInfraType() {
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir) throws BuildContextException {
        ObjectMapper mapper = new ObjectMapper();
        EnVarsGetter enVarsGetter = new EnVarsGetter();
        Map<String, Object> infraMap = enVarsGetter.gatherInfraInfo();
        try {
            mapper.writeValue(new File(outputCaptureDir, "infra.json"), infraMap);
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        }
    }

    @Extension
    @SuppressWarnings("unused")
    public static class JobInfraTypeDescriptor extends BuildContextCaptureTypeDescriptor<JobInfraType> {

        @Override
        public Class<? extends BuildContextCaptureType> getType() {
            return JobInfraType.class;
        }

        @Override
        public String getDisplayName() {
            return "Job infrastructure files";
        }

        @Override
        public String getLabel() {
            return "Capture " + getDisplayName();
        }
    }
}
