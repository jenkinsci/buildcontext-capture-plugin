package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.service.InfraGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class JobInfraType extends BuildContextCaptureType {

    @DataBoundConstructor
    public JobInfraType() {
    }

    @Override
    protected String getFileName() {
        return "infra";
    }

    @Override
    public void capture(AbstractBuild build, File outputCaptureDir, String format) throws BuildContextException {
        InfraGetter infraGetter = new InfraGetter();
        Map<String, Object> infraMap = infraGetter.gatherInfraInfo();
        export(infraMap, outputCaptureDir, format);
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
