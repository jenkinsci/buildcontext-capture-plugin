package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.model.Descriptor;

/**
 * @author Gregory Boissinot
 */
public abstract class FlexibleBuildContextCaptureTypeDescriptor<T extends BuildContextCaptureType> extends Descriptor<FlexibleBuildContextCaptureType> {

    public abstract String getLabel();

    public abstract Class<? extends FlexibleBuildContextCaptureType> getType();

    @SuppressWarnings("unused")
    public String getTypeName() {
        return getType().getName();
    }

}
