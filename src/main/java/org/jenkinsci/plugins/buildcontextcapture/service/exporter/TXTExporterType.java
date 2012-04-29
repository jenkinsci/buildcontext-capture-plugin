package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import hudson.FilePath;
import hudson.remoting.Callable;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class TXTExporterType extends ExporterType {

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
                    try {
                        File destinationFile = new File(outputDir.getRemote(), fileName + getExtension());
                        destinationFile.createNewFile();
                        FileWriter fileWriter = new FileWriter(destinationFile);
                        exportMap(map, fileWriter);
                        fileWriter.close();
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

    @SuppressWarnings("unchecked")
    private void exportMap(Map<String, ? extends Object> map, FileWriter fileWriter) throws IOException {
        for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            //Flatten map
            if (value instanceof Map) {
                exportMap((Map<String, Object>) value, fileWriter);
            } else {
                fileWriter.write(String.format("%s=%s\n", entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
    }

    @Override
    public String getExtension() {
        return ".txt";
    }

}
