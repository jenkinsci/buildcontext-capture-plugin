package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.model.Descriptor;

/**
 * @author Gregory Boissinot
 */
public abstract class BuildContextCaptureTypeDescriptor<T extends BuildContextCaptureType> extends Descriptor<BuildContextCaptureType> {

    public abstract String getLabel();

    public abstract Class<? extends BuildContextCaptureType> getType();

    @SuppressWarnings("unused")
    public String getTypeName() {
        return getType().getName();
    }

}
