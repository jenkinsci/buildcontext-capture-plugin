package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextCaptureAction;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class BuildContextExporterRetriever implements Serializable {

    public ExporterType getExportedFormat(AbstractBuild build) {
        return getSerializationFormat(getSerializationFormat(build));
    }

    private String getSerializationFormat(AbstractBuild build) {
        BuildContextCaptureAction buildContextCaptureAction = build.getAction(BuildContextCaptureAction.class);
        if (buildContextCaptureAction != null) {
            return buildContextCaptureAction.getExportedFormat();
        }
        return null;
    }

    private ExporterType getSerializationFormat(String format) {

        if (format == null) {
            return new TXTExporterType();
        }

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
