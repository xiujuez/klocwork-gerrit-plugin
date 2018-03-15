package org.jenkinsci.plugins.klocworkgerrit.review;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public interface ConnectionInfo {
    String getServerName();

    String getChangeNumber();

    String getPatchsetNumber();

    String getUsername();

    String getPassword();
}
