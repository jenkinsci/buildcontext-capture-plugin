package org.jenkinsci.plugins.buildcontextcapture;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import org.codehaus.jackson.map.ObjectMapper;
import org.jenkinsci.plugins.buildcontextcapture.service.EnVarsGetter;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gregory Boissinot
 */
@Extension
public class BuildContextListener extends RunListener<Run> {

    private static Logger LOGGER = Logger.getLogger(BuildContextListener.class.getName());

    @Override
    public void onCompleted(Run run, TaskListener listener) {

        EnVarsGetter enVarsGetter = new EnVarsGetter();
        ObjectMapper mapper = new ObjectMapper();
        AbstractBuild build = (AbstractBuild) run;

        try {
            //Gather infra info
            Map<String, Object> infraInfo = enVarsGetter.gatherInfraInfo();
            mapper.writeValue(getInfraStoredFile(build), infraInfo);

            //Gather build jobs env vars
            Map<String, String> jobEnvVars = enVarsGetter.gatherJobEnvVars(build);
            mapper.writeValue(getJobEnvsStoredFile(build), jobEnvVars);

        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Problems occurs to capture build context: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (BuildContextException be) {
            LOGGER.log(Level.SEVERE, "Problems occurs to capture build context: " + be.getMessage());
            be.printStackTrace();
        }
    }


    private File getInfraStoredFile(AbstractBuild build) {
        return new File(build.getRootDir(), "buildContext-infra.json");
    }

    private File getJobEnvsStoredFile(AbstractBuild build) {
        return new File(build.getRootDir(), "buildContext-job.json");
    }
}
