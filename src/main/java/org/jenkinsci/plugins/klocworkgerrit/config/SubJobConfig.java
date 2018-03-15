package org.jenkinsci.plugins.klocworkgerrit.config;

import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 *
 */

public class SubJobConfig extends AbstractDescribableImpl<SubJobConfig> {

    @Nonnull
    private String projectPath;

    @Nonnull
    private String klocworkReportPath;

    private boolean autoMatch;

    @SuppressFBWarnings(value="NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR") // values initialized by setters in constructor
    public SubJobConfig(String projectPath, String klocworkReportPath) {
        setProjectPath(projectPath);
        setKlocworkReportPath(klocworkReportPath);
        setAutoMatch(InspectionConfig.DescriptorImpl.AUTO_MATCH);
    }

    @DataBoundConstructor
    public SubJobConfig() {
        this(DescriptorImpl.PROJECT_PATH, DescriptorImpl.KLOCWORK_REPORT_PATH);
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getKlocworkReportPath() {
        return klocworkReportPath;
    }

    public boolean isAutoMatch() {
        return autoMatch;
    }

    @DataBoundSetter
    public void setAutoMatch(boolean autoMatch) {
        this.autoMatch = autoMatch;
    }

    @DataBoundSetter
    public void setProjectPath(String projectPath) {
        this.projectPath = MoreObjects.firstNonNull(projectPath, KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH);
    }

    @DataBoundSetter
    public void setKlocworkReportPath(String klocworkReportPath) {
        this.klocworkReportPath = MoreObjects.firstNonNull(klocworkReportPath, KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return new DescriptorImpl();
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<SubJobConfig> {
        public static final String PROJECT_PATH = KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH;
        public static final String KLOCWORK_REPORT_PATH = KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH;

//        /**
//         * Performs on-the-fly validation of the form field 'serverURL'.
//         *
//         * @param value This parameter receives the value that the user has typed.
//         *
//         * @return Indicates the outcome of the validation. This is sent to the browser.
//         * <p>
//         * Note that returning {@link FormValidation#error(String)} does not
//         * prevent the form from being saved. It just means that a message
//         * will be displayed to the user.
//         */
//        @SuppressWarnings(value = "unused")
//        public FormValidation doCheckServerURL(@QueryParameter String value) {
//            if (Util.fixEmptyAndTrim(value) == null) {
//                return FormValidation.warning(getLocalized("jenkins.plugin.error.klocwork.url.empty"));
//            }
//            try {
//                new URL(value);
//            } catch (MalformedURLException e) {
//                return FormValidation.warning(getLocalized("jenkins.plugin.error.klocwork.url.invalid"));
//            }
//            return FormValidation.ok();
//
//        }

        public String getDisplayName() {
            return "SubJobConfig";
        }
    }
}


