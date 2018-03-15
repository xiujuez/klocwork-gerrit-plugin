package org.jenkinsci.plugins.klocworkgerrit.review;

import com.google.common.base.MoreObjects;
import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritManagement;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.IGerritHudsonTriggerConfig;
import com.urswolfer.gerrit.client.rest.GerritAuthData;
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory;
import org.jenkinsci.plugins.klocworkgerrit.util.DataHelper;

import static org.jenkinsci.plugins.klocworkgerrit.util.Localization.getLocalized;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public class GerritConnector {

    private GerritApi gerritApi;
    private ConnectionInfo connectionInfo;


    public GerritConnector(GerritConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public boolean isConnected() {
        return gerritApi != null;
    }

    public void connect() {
        String serverName = connectionInfo.getServerName();

        IGerritHudsonTriggerConfig gerritConfig = GerritManagement.getConfig(serverName);
        DataHelper.checkNotNull(gerritConfig, "jenkins.plugin.error.gerrit.config.empty");

        String gerritFrontEndUrl = gerritConfig.getGerritFrontEndUrl();

        boolean useRestApi = gerritConfig.isUseRestApi();
        checkRestApiAllowed(useRestApi);

        String username = getUsername(connectionInfo.getUsername(), gerritConfig);
        String password = getPassword(connectionInfo.getUsername(), gerritConfig);
        DataHelper.checkNotEmpty(username, "jenkins.plugin.error.gerrit.user.empty");

        GerritRestApiFactory gerritRestApiFactory = new GerritRestApiFactory();
        GerritAuthData.Basic authData = new GerritAuthData.Basic(gerritFrontEndUrl, username, password, useRestApi);
        gerritApi = gerritRestApiFactory.create(authData);
    }

    public RevisionApi getRevision() throws RestApiException {
        return gerritApi.changes().id(connectionInfo.getChangeNumber()).revision(connectionInfo.getPatchsetNumber());
    }

    private void checkRestApiAllowed(boolean useRestApi) {
        if (!useRestApi) {
            throw new IllegalStateException(getLocalized("jenkins.plugin.error.gerrit.restapi.off"));
        }
    }

    private String getUsername(String username, IGerritHudsonTriggerConfig gerritConfig) {
        return MoreObjects.firstNonNull(username, gerritConfig.getGerritHttpUserName());
    }

    private String getPassword(String password, IGerritHudsonTriggerConfig gerritConfig) {
        return MoreObjects.firstNonNull(password, gerritConfig.getGerritHttpPassword());
    }


}
