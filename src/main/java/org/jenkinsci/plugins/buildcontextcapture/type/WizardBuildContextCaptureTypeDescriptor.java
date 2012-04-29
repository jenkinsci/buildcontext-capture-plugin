package org.jenkinsci.plugins.buildcontextcapture.type;

import hudson.model.Descriptor;

/**
 * @author Gregory Boissinot
 */
public abstract class WizardBuildContextCaptureTypeDescriptor<T extends WizardBuildContextCaptureType> extends Descriptor<WizardBuildContextCaptureType> {

    public abstract String getLabel();

    public abstract Class<? extends WizardBuildContextCaptureType> getType();

    @SuppressWarnings("unused")
    public String getTypeName() {
        return getType().getName();
    }

}

