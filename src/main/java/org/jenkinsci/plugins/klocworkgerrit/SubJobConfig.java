package org.jenkinsci.plugins.klocworkgerrit;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */

public class SubJobConfig extends AbstractDescribableImpl<SubJobConfig> {
    private String workspacePath;
    private String klocworkReportPath;

    @DataBoundConstructor
    public SubJobConfig(String projectPath, String klocworkReportPath) {
        this.workspacePath = projectPath;
        this.klocworkReportPath = klocworkReportPath;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }
    public String getKlocworkReportPath() {
        return klocworkReportPath;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<SubJobConfig> {
        public String getDisplayName() {
            return "SubJobConfig";
        }
    }

}


