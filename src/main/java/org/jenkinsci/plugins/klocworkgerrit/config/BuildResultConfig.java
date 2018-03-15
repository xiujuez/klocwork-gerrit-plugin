package org.jenkinsci.plugins.klocworkgerrit.config;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * $Id$
 */

public class BuildResultConfig extends AbstractDescribableImpl<BuildResultConfig> {
    private boolean markFailure;

    public BuildResultConfig(boolean markFailure) {
        setMarkFailure(markFailure);
    }

    @DataBoundConstructor
    public BuildResultConfig() {
        this(DescriptorImpl.MARK_FAILURE);
    }

    public boolean getMarkFailure() {
        return markFailure;
    }

    @DataBoundSetter
    public void setMarkFailure(boolean markFailure) {
        this.markFailure = markFailure;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return new DescriptorImpl();
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<BuildResultConfig> {
        public static final boolean MARK_FAILURE = KlocworkToGerritPublisher.DescriptorImpl.MARK_FAILURE;

        public String getDisplayName() {
            return "BuildResultConfig";
        }
    }
}
