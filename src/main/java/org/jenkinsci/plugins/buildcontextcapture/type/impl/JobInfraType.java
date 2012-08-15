package org.jenkinsci.plugins.buildcontextcapture.type.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.service.InfraGetter;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.type.WizardBuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class JobInfraType extends WizardBuildContextCaptureType {

    @DataBoundConstructor
    public JobInfraType() {
    }

    @Override
    protected Map<String, ? extends Object> getCapturedElements(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {
        InfraGetter infraGetter = new InfraGetter();
        return infraGetter.gatherInfraInfo();
    }

    @Override
    protected String getFileName() {
        return "infra";
    }

    @Extension
    @SuppressWarnings("unused")
    public static class JobInfraTypeDescriptor extends BuildContextCaptureTypeDescriptor<JobInfraType> {

        @Override
        public Class<? extends WizardBuildContextCaptureType> getType() {
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
