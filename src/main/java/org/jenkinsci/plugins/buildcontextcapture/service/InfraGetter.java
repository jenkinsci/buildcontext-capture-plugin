package org.jenkinsci.plugins.buildcontextcapture.service;

import hudson.PluginWrapper;
import hudson.model.Hudson;
import org.jenkinsci.plugins.buildcontextcapture.BuildContextException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class InfraGetter implements Serializable {

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

    private Map<String, String> gatherHudsonPlugins() throws BuildContextException {
        Map<String, String> pluginsInfo = new HashMap<String, String>();
        List<PluginWrapper> plugins = Hudson.getInstance().getPluginManager().getPlugins();
        for (PluginWrapper pluginWrapper : plugins) {
            pluginsInfo.put(pluginWrapper.getShortName(), pluginWrapper.getVersion());
        }
        return pluginsInfo;
    }

}
