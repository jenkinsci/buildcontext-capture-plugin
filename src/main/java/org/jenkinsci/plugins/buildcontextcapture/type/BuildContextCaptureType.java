package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
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

    public abstract void capture(AbstractBuild build, File outputCaptureDir, String format) throws BuildContextException;

    protected abstract String getFileName();

    protected void export(Map<String, ? extends Object> mapValues, File outputCaptureDir, String format) throws BuildContextException {
        AbstractExporterType type = getSerializerFormat(format);
        outputCaptureDir.mkdirs();
        File destFile = new File(outputCaptureDir, getFileName() + type.getExtension());
        type.toExport(mapValues, destFile);
    }

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
