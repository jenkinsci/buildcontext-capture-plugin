package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public abstract class AbstractExporterType implements Serializable {

    public abstract void toExport(Map<String, ? extends Object> map, File outFile) throws BuildContextException;

    public abstract String getExtension();
}
