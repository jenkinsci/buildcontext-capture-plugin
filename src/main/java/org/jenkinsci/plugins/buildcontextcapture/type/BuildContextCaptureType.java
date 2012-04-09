package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.AbstractExporterType;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.JSONExporterType;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.TXTExporterType;
import org.jenkinsci.plugins.buildcontextcapture.service.exporter.XMLExporterType;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class BuildContextCaptureType implements ExtensionPoint, Describable<BuildContextCaptureType>, Serializable {

    @SuppressWarnings("unchecked")
    public Descriptor<BuildContextCaptureType> getDescriptor() {
        return (BuildContextCaptureTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    public void capture(AbstractBuild build, BuildContextLogger logger, final File outputCaptureDir, String format) throws BuildContextException {
        outputCaptureDir.mkdirs();
        captureAndExport(build, logger, outputCaptureDir, format);
    }

    protected void captureAndExport(AbstractBuild build, BuildContextLogger logger, File outputCaptureDir, String format) throws BuildContextException {
        final Map<String, ? extends Object> capturedElements = getCapturedElements(build, logger);
        if (capturedElements != null && capturedElements.size() != 0) {
            final AbstractExporterType type = getSerializerFormat(format);
            File destFile = new File(outputCaptureDir, getFileName() + type.getExtension());
            type.toExport(capturedElements, destFile);
        }
    }

    public abstract Map<String, ? extends Object> getCapturedElements(AbstractBuild build, BuildContextLogger logger) throws BuildContextException;

    protected abstract String getFileName();

    protected AbstractExporterType getSerializerFormat(String format) {

        if ("TXT".equalsIgnoreCase(format)) {
            return new TXTExporterType();
        }

        if ("XML".equalsIgnoreCase(format)) {
            return new XMLExporterType();
        }

        if ("JSON".equalsIgnoreCase(format)) {
            return new JSONExporterType();
        }

        return new TXTExporterType();
    }


}
