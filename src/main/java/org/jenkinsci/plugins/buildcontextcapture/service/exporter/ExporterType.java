package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import hudson.FilePath;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class ExporterType implements Serializable {

    public abstract void toExport(Map<String, ? extends Object> map, FilePath outputDir, String fileName) throws BuildContextException;

    public abstract String getExtension();
}
