package org.jenkinsci.plugins.buildcontextcapture;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.buildcontextcapture.type.BuildContextCaptureType;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BuildContextJobProperty extends JobProperty {

    private boolean on;

    private BuildContextCaptureType[] types;

    public boolean isOn() {
        return on;
    }

    public BuildContextCaptureType[] getTypes() {
        return types;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setTypes(BuildContextCaptureType[] types) {
        this.types = types;
    }


    @SuppressWarnings("unused")
    @Extension
    public static class BuildContextJobPropertyDescriptor extends JobPropertyDescriptor {

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @SuppressWarnings({"unchecked", "unused"})
        public DescriptorExtensionList getListBuildContextCaptureDescriptors() {
            return DescriptorExtensionList.createDescriptorList(Hudson.getInstance(), BuildContextCaptureType.class);
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            BuildContextJobProperty buildContextJobProperty = new BuildContextJobProperty();
            Object onObject = formData.get("on");
            if (onObject != null) {

                JSONObject jsonObject = (JSONObject) onObject;
                JSON elts;
                try {
                    elts = jsonObject.getJSONArray("buildContextTypes");
                } catch (JSONException jsone) {
                    elts = jsonObject.getJSONObject("buildContextTypes");
                }
                List<BuildContextCaptureType> types = req.bindJSONToList(BuildContextCaptureType.class, elts);
                buildContextJobProperty.setOn(true);
                buildContextJobProperty.setTypes(types.toArray(new BuildContextCaptureType[types.size()]));
            }
            return buildContextJobProperty;
        }
    }
}
