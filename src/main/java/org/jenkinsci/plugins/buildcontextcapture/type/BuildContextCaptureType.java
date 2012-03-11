package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.ExtensionPoint;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.File;
import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public abstract class BuildContextCaptureType implements ExtensionPoint, Describable<BuildContextCaptureType>, Serializable {

    public Descriptor<BuildContextCaptureType> getDescriptor() {
        return (BuildContextCaptureTypeDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }

    public abstract void capture(AbstractBuild build, File outputCaptureDir) throws BuildContextException;

}
