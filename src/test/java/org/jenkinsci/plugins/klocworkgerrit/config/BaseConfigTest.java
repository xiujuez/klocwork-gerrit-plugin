package org.jenkinsci.plugins.klocworkgerrit.config;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public interface BaseConfigTest {
    void testFilterConfig();

    void testReviewConfig();

    void testScoreConfig();

    void testNotificationConfig();

    void testAuthenticationConfig();

    void testInspectionConfig();
}
