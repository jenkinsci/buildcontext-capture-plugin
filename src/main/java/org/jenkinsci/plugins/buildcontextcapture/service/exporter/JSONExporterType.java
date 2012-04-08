package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import org.codehaus.jackson.map.ObjectMapper;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class JSONExporterType extends AbstractExporterType {

    @Override
    public void toExport(Map<String, ? extends Object> map, File outFile) throws BuildContextException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(outFile, map);
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        }
    }

    @Override
    public String getExtension() {
        return ".json";
    }
}
