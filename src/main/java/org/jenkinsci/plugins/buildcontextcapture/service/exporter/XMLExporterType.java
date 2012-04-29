package org.jenkinsci.plugins.buildcontextcapture.service.exporter;

import com.thoughtworks.xstream.XStream;
import hudson.FilePath;
import hudson.remoting.Callable;
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
public class XMLExporterType extends ExporterType {

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
                    XStream xStream = new XStream2();
                    FileOutputStream fileOutputStream = null;
                    try {
                        File destinationFile = new File(outputDir.getRemote(), fileName + getExtension());
                        destinationFile.createNewFile();
                        fileOutputStream = new FileOutputStream(destinationFile);
                        xStream.toXML(map, fileOutputStream);
                    } catch (FileNotFoundException fne) {
                        throw new BuildContextException(fne);
                    } catch (IOException ioe) {
                        throw new BuildContextException(ioe);
                    } finally {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException ioe) {
                                throw new BuildContextException(ioe);
                            }
                        }
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
        return ".xml";
    }
}
