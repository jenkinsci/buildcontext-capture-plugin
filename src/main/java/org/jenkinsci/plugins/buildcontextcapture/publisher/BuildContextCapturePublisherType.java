package org.jenkinsci.plugins.buildcontextcapture.publisher;

import hudson.Extension;
import hudson.model.AbstractBuild;
import org.jenkinsci.lib.xpublisher.XPublisherArtifact;
import org.jenkinsci.lib.xpublisher.XPublisherException;
import org.jenkinsci.lib.xpublisher.XPublisherLogger;
import org.jenkinsci.lib.xpublisher.types.XPublisherType;
import org.jenkinsci.lib.xpublisher.types.XPublisherTypeDescriptor;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextJobProperty;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCapturePublisherType extends XPublisherType {

    @DataBoundConstructor
    public BuildContextCapturePublisherType() {
    }

    @Override
    public List<XPublisherArtifact> getArtifacts(AbstractBuild build, XPublisherLogger xPublisherLogger) throws XPublisherException {
        BuildContextLogger logger = new BuildContextLogger(xPublisherLogger.getListener());
        List<XPublisherArtifact> result = new ArrayList<XPublisherArtifact>();

        BuildContextJobProperty buildContextJobProperty = (BuildContextJobProperty) build.getProject().getProperty(BuildContextJobProperty.class);
        if (buildContextJobProperty == null) {
            logger.info("No captured elements. There there are no build context elements to publish.");
            return result;
        }

        BuildContextCaptureType[] types = buildContextJobProperty.getTypes();
        if (types == null || types.length == 0) {
            logger.info("No build context elements to publish.");
        }

        for (BuildContextCaptureType type : types) {
            XPublisherArtifact xPublisherArtifact = null;
            try {
                xPublisherArtifact = new XPublisherArtifact("buildContext", type.getExportedFilePath(build, logger));
            } catch (BuildContextException bce) {
                throw new XPublisherException(bce);
            }
            result.add(xPublisherArtifact);
        }

        return result;
    }

    @Extension
    public static class BuildContextPublisherDescriptor extends XPublisherTypeDescriptor<BuildContextCapturePublisherType> {

        @Override
        public String getLabel() {
            return "BuildContext capture elements";
        }

        @Override
        public Class<?> getType() {
            return BuildContextCapturePublisherType.class;
        }

        @Override
        public String getDisplayName() {
            return "Publish " + getLabel();
        }
    }

}
