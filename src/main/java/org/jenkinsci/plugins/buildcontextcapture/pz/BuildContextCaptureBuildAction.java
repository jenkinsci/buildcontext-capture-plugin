package org.jenkinsci.plugins.buildcontextcapture.pz;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCaptureBuildAction extends BuildContextCaptureAction {

    private List<BuildContextCaptureUIElement> elements = new ArrayList<BuildContextCaptureUIElement>();

    public BuildContextCaptureBuildAction(List<BuildContextCaptureUIElement> elements) {
        this.elements = elements;
    }

    public List<BuildContextCaptureUIElement> getElements() {
        return elements;
    }

}
