package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import hudson.FilePath;
import hudson.remoting.Callable;
import org.codehaus.jackson.map.ObjectMapper;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class JSONExporterType extends ExporterType {

    @Override
    public void toExport(final Map<String, ? extends Object> map, final FilePath outputDir, final String fileName) throws BuildContextException {

        if (map == null) {
            throw new NullPointerException("A map content is required.");
        }

        if (outputDir == null) {
            throw new NullPointerException("A target directory is required.");
        }

        try {
            outputDir.act(new Callable<Void, BuildContextException>() {
                public Void call() throws BuildContextException {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        File destinationFile = new File(outputDir.getRemote(), fileName + getExtension());
                        destinationFile.createNewFile();
                        mapper.writeValue(destinationFile, map);
                    } catch (IOException ioe) {
                        throw new BuildContextException(ioe);
                    }
                    return null;
                }
            });
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
        } catch (InterruptedException ie) {
            throw new BuildContextException(ie);
        }
    }

    @Override
    public String getExtension() {
        return ".json";
    }
}
