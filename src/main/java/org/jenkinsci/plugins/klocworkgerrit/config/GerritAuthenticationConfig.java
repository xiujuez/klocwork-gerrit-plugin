package org.jenkinsci.plugins.klocworkgerrit.config;

import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritManagement;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.IGerritHudsonTriggerConfig;
import hudson.Extension;
import hudson.util.FormValidation;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.jenkinsci.plugins.klocworkgerrit.util.Localization.getLocalized;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * $Id$
 */
public class GerritAuthenticationConfig extends AuthenticationConfig {
    public GerritAuthenticationConfig(@Nonnull String username, @Nonnull String password) {
        super(username, password);
    }

    @DataBoundConstructor
    public GerritAuthenticationConfig() {
        super();
    }

    @Override
    @DataBoundSetter
    public void setPassword(@Nonnull String password) {
        super.setPassword(password);
    }

    @Override
    @DataBoundSetter
    public void setUsername(@Nonnull String username) {
        super.setUsername(username);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return new DescriptorImpl();
    }

    @Extension
    public static class DescriptorImpl extends AuthenticationConfig.DescriptorImpl {

        public FormValidation doTestConnection(@QueryParameter("username") final String username,
                                               @QueryParameter("password") final String password,
                                               @QueryParameter("serverName") final String serverName) throws IOException, ServletException {
            doCheckUsername(username);
            doCheckPassword(password);

            IGerritHudsonTriggerConfig gerritConfig = GerritManagement.getConfig(serverName);
            if (gerritConfig == null) {
                return FormValidation.error(getLocalized("jenkins.plugin.error.gerrit.config.empty"));
            }

            if (!gerritConfig.isUseRestApi()) {
                return FormValidation.error(getLocalized("jenkins.plugin.error.gerrit.restapi.off"));
            }

            GerritServer server = PluginImpl.getServer_(serverName);
            if (server == null) {
                return FormValidation.error(getLocalized("jenkins.plugin.error.gerrit.server.empty"));
            }
            return server.getDescriptor().doTestRestConnection(gerritConfig.getGerritFrontEndUrl(), username, password/*, gerritConfig.isUseRestApi()*/);

        }

        public List<String> getServerNames() {
            return PluginImpl.getServerNames_();
        }


        public String getDisplayName() {
            return "GerritAuthenticationConfig";
        }
    }
}
