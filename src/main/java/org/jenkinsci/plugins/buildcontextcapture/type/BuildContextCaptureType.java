package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.model.*;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
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

    public void capture(AbstractBuild build, TaskListener listener, final File outputCaptureDir, String format) throws BuildContextException {
        outputCaptureDir.mkdirs();
        captureAndExport(build, listener, outputCaptureDir, format);
    }

    public void captureAndExport(AbstractBuild build, TaskListener listener, final File outputCaptureDir, String format) throws BuildContextException {
        final Map<String, ? extends Object> capturedElements = getCapturedElements(build, listener);
        if (capturedElements != null && capturedElements.size() != 0) {
            final AbstractExporterType type = getSerializerFormat(format);
            File destFile = new File(outputCaptureDir, getFileName() + type.getExtension());
            type.toExport(capturedElements, destFile);
        }
    }

    public abstract Map<String, ? extends Object> getCapturedElements(AbstractBuild build, TaskListener listener) throws BuildContextException;

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
