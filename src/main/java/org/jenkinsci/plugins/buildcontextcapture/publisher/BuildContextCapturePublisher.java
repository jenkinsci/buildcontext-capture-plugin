package org.jenkinsci.plugins.buildcontextcapture.publisher;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import org.jenkinsci.lib.xpublisher.XPublisherArtifact;
import org.jenkinsci.lib.xpublisher.XPublisherException;
import org.jenkinsci.lib.xpublisher.XPublisherLogger;
import org.jenkinsci.lib.xpublisher.types.XPublisherType;
import org.jenkinsci.lib.xpublisher.types.XPublisherTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.type.FlexibleBuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCapturePublisher extends XPublisherType {

    @DataBoundConstructor
    public BuildContextCapturePublisher() {
    }

    @Override
    public List<XPublisherArtifact> getArtifacts(AbstractBuild build, XPublisherLogger xPublisherLogger) throws XPublisherException {

        List<XPublisherArtifact> result = new ArrayList<XPublisherArtifact>();

        BuildContextLogger logger = new BuildContextLogger(xPublisherLogger.getListener());
        ExtensionList<FlexibleBuildContextCaptureType> extensionList = Hudson.getInstance().getExtensionList(FlexibleBuildContextCaptureType.class);
        for (FlexibleBuildContextCaptureType flexibleBuildContextCaptureType : extensionList) {
            try {
                FilePath buildContextCaptureFilePath = flexibleBuildContextCaptureType.getExportedFilePath(build, logger);
                XPublisherArtifact xPublisherArtifact = new XPublisherArtifact(
                        "buildContext",
                        buildContextCaptureFilePath.getName(),
                        buildContextCaptureFilePath.toURI().toString());
            } catch (BuildContextException be) {
                throw new XPublisherException(be);
            } catch (InterruptedException ie) {
                throw new XPublisherException(ie);
            } catch (IOException ioe) {
                throw new XPublisherException(ioe);
            }
        }

        return result;
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
