package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.BuildContextExporterRetriever;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.ExporterType;

import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class WizardBuildContextCaptureType extends BuildContextCaptureType {

    @Override
    public void capture(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {

        final Map<String, ? extends Object> capturedElements = getCapturedElements(build, logger);
        if (capturedElements != null && capturedElements.size() != 0) {

            //Get exported directory
            FilePath exportedDir = getExportedDir(build);
            try {
                exportedDir.mkdirs();
            } catch (IOException ioe) {
                throw new BuildContextException(ioe);
            } catch (InterruptedException ie) {
                throw new BuildContextException(ie);
            }

            //Get exporter
            BuildContextExporterRetriever exporterRetriever = new BuildContextExporterRetriever();
            ExporterType exporterType = exporterRetriever.getExportedFormat(build);

            //Export captured elements
            exporterType.toExport(capturedElements, exportedDir, getFileName());
        }
    }

    @Override
    public FilePath getExportedFilePath(AbstractBuild build, BuildContextLogger logger) throws BuildContextException {
        FilePath exportedDir = getExportedDir(build);
        return getExportedFilePath(build, logger, exportedDir);
    }

    private FilePath getExportedFilePath(AbstractBuild build, BuildContextLogger logger, FilePath outputDirectory) throws BuildContextException {
        BuildContextExporterRetriever exporterRetriever = new BuildContextExporterRetriever();
        ExporterType exporterType = exporterRetriever.getExportedFormat(build);
        return outputDirectory.child(getFileName() + exporterType.getExtension());
    }

    protected abstract Map<String, ? extends Object> getCapturedElements(AbstractBuild build, BuildContextLogger logger) throws BuildContextException;

    protected abstract String getFileName();
}
