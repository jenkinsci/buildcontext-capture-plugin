package org.jenkinsci.plugins.buildcontextcapture;

import hudson.Extension;
import hudson.matrix.MatrixRun;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import hudson.remoting.Callable;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gregory Boissinot
 */
@Extension
public class BuildContextListener extends RunListener<Run> {

    private static Logger LOGGER = Logger.getLogger(BuildContextListener.class.getName());

    @Override
    public void onCompleted(final Run run, final TaskListener listener) {

        try {
            Hudson.getInstance().getRootPath().act(new Callable() {

                public Void call() throws Throwable {
                    listener.getLogger().println("Capturing build context.");
                    AbstractBuild build = (AbstractBuild) run;
                    try {
                        BuildContextJobProperty buildContextJobProperty = getEnvInjectJobProperty(build);
                        if (buildContextJobProperty != null) {
                            BuildContextJobProperty.BuildContextJobPropertyDescriptor descriptor = (BuildContextJobProperty.BuildContextJobPropertyDescriptor) buildContextJobProperty.getDescriptor();
                            build.addAction(new BuildContextCaptureAction(descriptor.getFormat()));
                            BuildContextCaptureType[] captureTypes = buildContextJobProperty.getTypes();
                            if (captureTypes != null) {
                                for (BuildContextCaptureType captureType : captureTypes) {
                                    BuildContextLogger logger = new BuildContextLogger(listener);
                                    captureType.capture(build, logger);
                                }
                            }
                        }
                    } catch (BuildContextException be) {
                        LOGGER.log(Level.SEVERE, "Problems occurs to capture build context: " + be.getMessage());
                        be.printStackTrace();
                    }

                    return null;
                }
            });
        } catch (Throwable throwable) {
            listener.getLogger().println("BuildContextCapture - Error :" + throwable.getMessage());
        }
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
