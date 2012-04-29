package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.remoting.Callable;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextLogger;

import java.io.File;
import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public abstract class BuildContextCaptureType implements Serializable {

    public abstract void capture(AbstractBuild build, BuildContextLogger logger) throws BuildContextException;

    public abstract FilePath getExportedFilePath(AbstractBuild build, BuildContextLogger logger) throws BuildContextException;

    protected FilePath getExportedDir(AbstractBuild build) throws BuildContextException {
        final File rootDirFile = build.getRootDir();
        try {
            return Hudson.getInstance().getRootPath().act(new Callable<FilePath, Exception>() {
                public FilePath call() throws Exception {
                    return new FilePath(new File(rootDirFile, "buildContext"));
                }
            });
        } catch (Exception e) {
            throw new BuildContextException(e);
        }
    }
}
