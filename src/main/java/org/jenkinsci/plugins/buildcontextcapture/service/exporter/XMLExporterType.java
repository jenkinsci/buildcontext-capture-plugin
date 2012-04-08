package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import com.thoughtworks.xstream.XStream;
import hudson.util.XStream2;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class XMLExporterType extends AbstractExporterType {

    @Override
    public void toExport(Map<String, ? extends Object> map, File outFile) throws BuildContextException {
        XStream xStream = new XStream2();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outFile);
            xStream.toXML(map, fileOutputStream);
        } catch (FileNotFoundException fne) {
            throw new BuildContextException(fne);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ioe) {
                    throw new BuildContextException(ioe);
                }
            }
        }
    }

    @Override
    public String getExtension() {
        return ".xml";
    }

}
