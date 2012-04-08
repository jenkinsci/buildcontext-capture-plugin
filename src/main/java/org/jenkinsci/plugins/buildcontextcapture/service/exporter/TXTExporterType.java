package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class TXTExporterType extends AbstractExporterType {

    @Override
    public void toExport(Map<String, ? extends Object> map, File outFile) throws BuildContextException {
        try {
            FileWriter fileWriter = new FileWriter(outFile);
            exportMap(map, fileWriter);
            fileWriter.close();
        } catch (IOException ioe) {
            throw new BuildContextException(ioe);
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
