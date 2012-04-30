package org.jenkinsci.plugins.buildcontextcapture.pz;

import hudson.model.Action;
import hudson.model.Hudson;
import hudson.model.Job;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Gregory Boissinot
 */
public class BuildContextCaptureAction implements Action, Serializable {

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "capturedBuildContextElements";
    }

    @SuppressWarnings("unused")
    public void doDownload(StaplerRequest request, StaplerResponse response)
            throws IOException, ServletException {

        String localPath = request.getParameter("localPath");
        if (localPath == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A localPath parameter must be given.");
        }

        BuildContextCaptureUIElement uiElement = new BuildContextCaptureUIElement(localPath);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + uiElement.getFileName() + "\"");

        File rootDir = Hudson.getInstance().getRootDir();
        Job job = request.findAncestorObject(Job.class);
        String prefix = rootDir + "/jobs/" + job.getName();

        File buildContextCaptureFile = new File(prefix + "/" + localPath);
        if (buildContextCaptureFile.exists()) {
            response.serveFile(request, buildContextCaptureFile.toURI().toURL());
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);

    }

}
