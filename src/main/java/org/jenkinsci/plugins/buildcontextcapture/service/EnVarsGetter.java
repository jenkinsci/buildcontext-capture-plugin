package org.jenkinsci.plugins.buildcontextcapture.service;

import hudson.FilePath;
import hudson.PluginWrapper;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.util.LogTaskListener;
import org.jenkinsci.lib.envinject.EnvInjectAction;
import org.jenkinsci.lib.envinject.service.EnvInjectActionRetriever;
import org.jenkinsci.lib.envinject.service.EnvInjectDetector;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gregory Boissinot
 */
public class EnVarsGetter {

    private static Logger LOG = Logger.getLogger(EnVarsGetter.class.getName());

    public Map<String, String> gatherJobEnvVars(AbstractBuild build) throws BuildContextException{

        Map<String, String> result = new HashMap<String, String>();

        //Use the envInject var if exists
        EnvInjectDetector detector = new EnvInjectDetector();
        if (detector.isEnvInjectActivated(build)) {
            result.putAll(gatherEnvVarsFromEnvInject(build));
        } else {

            //Gather system env vars
            try {
                result.putAll(build.getEnvironment(new LogTaskListener(LOG, Level.ALL)));
            } catch (IOException e) {
                throw new BuildContextException(e);
            } catch (InterruptedException e) {
                throw new BuildContextException(e);
            }

            //Gather build vars
            result.putAll(gatherBuildVariables(build));
        }

        return result;
    }

    private Map<String, String> gatherBuildVariables(AbstractBuild build) throws BuildContextException{
        Map<String, String> result = new HashMap<String, String>();

        //Add build process variables
        result.putAll(build.getCharacteristicEnvVars());

        //Add build variables such as parameters, plugins contributions, ...
        result.putAll(build.getBuildVariables());

        //Add workspace variable
        FilePath ws = build.getWorkspace();
        if (ws != null) {
            result.put("WORKSPACE", ws.getRemote());
        }
        return result;
    }


    private Map<String, String> gatherEnvVarsFromEnvInject(AbstractBuild build) throws BuildContextException{
        Map<String, String> result = new HashMap<String, String>();
        EnvInjectActionRetriever retriever = new EnvInjectActionRetriever();
        EnvInjectAction envInjectAction = retriever.getEnvInjectAction(build);
        if (envInjectAction != null) {
            result.putAll(envInjectAction.getEnvMap());
        }
        return result;
    }

    /**
     * Gathers all information from the Jenkins infrastructure (Hudson version,
     * Plugins information, ...)
     *
     * @return the dictionary of the information
     */
    public Map<String, Object> gatherInfraInfo() throws BuildContextException {

        Map<String, Object> result = new HashMap<String, Object>();
        //Gets the jenkins version
        String version = Hudson.getVersion().toString();
        result.put("version", version);

        //Gets the plugins information
        Map<String, String> pluginsInfo = gatherHudsonPlugins();
        result.put("plugins", pluginsInfo);
        return result;
    }

    private Map<String, String> gatherHudsonPlugins() throws BuildContextException{
        Map<String, String> pluginsInfo = new HashMap<String, String>();
        List<PluginWrapper> plugins = Hudson.getInstance().getPluginManager().getPlugins();
        for (PluginWrapper pluginWrapper : plugins) {
            pluginsInfo.put(pluginWrapper.getLongName(), pluginWrapper.getVersion());
        }
        return pluginsInfo;
    }


}
