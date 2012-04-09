package org.jenkinsci.plugins.buildcontextcapture;

import hudson.model.TaskListener;

import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class BuildContextLogger implements Serializable {

    private TaskListener listener;

    public BuildContextLogger(TaskListener listener) {
        this.listener = listener;
    }

    public TaskListener getListener() {
        return listener;
    }

    public void info(String message) {
        listener.getLogger().println("[BuildContextCapture] - " + message);
    }

    public void error(String message) {
        listener.getLogger().println("[BuildContextCapture] - [ERROR] - " + message);
    }
}

