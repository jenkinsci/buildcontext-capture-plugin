package org.jenkinsci.plugins.buildcontextcapture;

import hudson.Extension;
import hudson.FilePath;
import hudson.matrix.MatrixRun;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import hudson.remoting.Callable;
import org.jenkinsci.plugins.buildcontextcapture.pz.BuildContextCaptureBuildAction;
import org.jenkinsci.plugins.buildcontextcapture.pz.BuildContextCaptureUIElement;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
@Extension
public class BuildContextListener extends RunListener<Run> {

    @Override
    public void onCompleted(final Run run, final TaskListener listener) {

        listener.getLogger().println("Capturing build context.");
        final AbstractBuild build = (AbstractBuild) run;
        final BuildContextLogger logger = new BuildContextLogger(listener);
        BuildContextJobProperty buildContextJobProperty = getEnvInjectJobProperty(build);
        if (buildContextJobProperty != null) {
            BuildContextJobProperty.BuildContextJobPropertyDescriptor descriptor = (BuildContextJobProperty.BuildContextJobPropertyDescriptor) buildContextJobProperty.getDescriptor();
            build.addAction(new BuildContextCaptureAction(descriptor.getFormat()));
            BuildContextCaptureType[] captureTypes = buildContextJobProperty.getTypes();
            if (captureTypes != null) {

                List<BuildContextCaptureUIElement> buildContextCaptureUIElements = new ArrayList<BuildContextCaptureUIElement>();
                for (final BuildContextCaptureType captureType : captureTypes) {

                    try {
                        Hudson.getInstance().getRootPath().act(new Callable() {
                            public Void call() throws Throwable {
                                captureType.capture(build, logger);
                                return null;
                            }
                        });

                        FilePath buildContextCaptureFilePath = captureType.getExportedFilePath(build, logger);
                        BuildContextCaptureUIElement uiElement = getBuildContextCaptureUIElement(build, buildContextCaptureFilePath);
                        buildContextCaptureUIElements.add(uiElement);

                    } catch (Throwable throwable) {
                        listener.getLogger().println("BuildContextCapture - Error :" + throwable.getMessage());
                    }

                }

                //Add build context action for display
                build.addAction(new BuildContextCaptureBuildAction(buildContextCaptureUIElements));
            }
        }
    }

    private BuildContextCaptureUIElement getBuildContextCaptureUIElement(AbstractBuild build, FilePath buildContextCaptureFilePath) {

        AbstractProject project = build.getProject();
        File jenkinsRootDir = Hudson.getInstance().getRootDir();
        String buildContextCaptureFilePathString = buildContextCaptureFilePath.getRemote();

        String startLocalPath = jenkinsRootDir + "/jobs/" + project.getName();
        String localPath = buildContextCaptureFilePathString.substring(buildContextCaptureFilePathString.indexOf(startLocalPath) + startLocalPath.length());

        BuildContextCaptureUIElement uiElement = new BuildContextCaptureUIElement(localPath);
        return uiElement;
    }

    @SuppressWarnings("unchecked")
    private BuildContextJobProperty getEnvInjectJobProperty(AbstractBuild build) {
        if (build == null) {
            throw new IllegalArgumentException("A build object must be set.");
        }

        Job job;
        if (build instanceof MatrixRun) {
            job = ((MatrixRun) build).getParentBuild().getParent();
        } else {
            job = build.getParent();
        }

        BuildContextJobProperty contextJobProperty = (BuildContextJobProperty) job.getProperty(BuildContextJobProperty.class);

        if (contextJobProperty != null) {
            if (contextJobProperty.isOn()) {
                return contextJobProperty;
            }
        }
        return null;
    }

}
