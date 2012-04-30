package org.jenkinsci.plugins.buildcontextcapture.pz;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCaptureProjectAction extends BuildContextCaptureAction {

    private AbstractProject<?, ?> project;

    public BuildContextCaptureProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    @SuppressWarnings("unused")
    public List<BuildContextCaptureUIElement> getElements() {
        BuildContextCaptureBuildAction lastBuildAction = getBuildContextCaptureBuildAction(project);
        if (lastBuildAction == null) {
            return new ArrayList<BuildContextCaptureUIElement>();
        }
        return lastBuildAction.getElements();

    }

    private BuildContextCaptureBuildAction getBuildContextCaptureBuildAction(AbstractProject<?, ?> project) {
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        if (lastBuild == null) {
            return null;
        }
        return lastBuild.getAction(BuildContextCaptureBuildAction.class);
    }
}
