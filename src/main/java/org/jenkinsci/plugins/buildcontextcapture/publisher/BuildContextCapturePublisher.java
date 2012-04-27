package org.jenkinsci.plugins.buildcontextcapture.publisher;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import org.jenkinsci.lib.xpublisher.XPublisherArtifact;
import org.jenkinsci.lib.xpublisher.XPublisherLogger;
import org.jenkinsci.lib.xpublisher.types.XPublisherType;
import org.jenkinsci.lib.xpublisher.types.XPublisherTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCapturePublisher extends XPublisherType {

    @DataBoundConstructor
    public BuildContextCapturePublisher() {
    }

    @Override
    public List<XPublisherArtifact> getArtifacts(AbstractBuild build, XPublisherLogger xPublisherLogger) {

        ExtensionList<BuildContextCaptureType> extensionList = Hudson.getInstance().getExtensionList(BuildContextCaptureType.class);

        return null;
    }

    @Extension
    public static class BuildContextPublisherDescriptor extends XPublisherTypeDescriptor<BuildContextCapturePublisher> {

        @Override
        public String getLabel() {
            return "BuildContext capture elements";
        }

        @Override
        public Class<?> getType() {
            return BuildContextCapturePublisher.class;
        }

        @Override
        public String getDisplayName() {
            return "Publish " + getLabel();
        }
    }

}
