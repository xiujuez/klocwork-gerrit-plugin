package org.jenkinsci.plugins.klocworkgerrit.signature;

import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */

/*
 * This class checks if it is still possible to keep configuration settings from previous plugin version
 * Methods left for back compatibility purposes should be @Deprecated
 */
public class BackCompatibilityConfigurationTest extends ConfigurationUpdateTest {

    @Test
    public void testSetSeverity() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String severity = "Warning";
        Assert.assertNotSame(severity, invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        Assert.assertNull(invokeGetter(p, "scoreConfig")); //
        invokeSetter(p, "severity", severity);

        Assert.assertEquals(severity, invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        severity = "Critical";
        Assert.assertNotSame(severity, invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        invokeSetter(p, "postScore", true);
        invokeSetter(p, "severity", severity);
        Assert.assertEquals(severity, invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        Assert.assertEquals(severity, invokeGetter(p, "scoreConfig", "issueFilterConfig", "severity"));
    }

    @Test
    public void testSetChangedLinesOnly() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        boolean changedLinesOnly = true;
        Assert.assertNotSame(changedLinesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));
        Assert.assertNull(invokeGetter(p, "scoreConfig")); //
        invokeSetter(p, "changedLinesOnly", changedLinesOnly);
        Assert.assertEquals(changedLinesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        changedLinesOnly = false;
        Assert.assertNotSame(changedLinesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));
        invokeSetter(p, "postScore", true);
        invokeSetter(p, "changedLinesOnly", changedLinesOnly);
        Assert.assertEquals(changedLinesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));
        Assert.assertEquals(changedLinesOnly, invokeGetter(p, "scoreConfig", "issueFilterConfig", "changedLinesOnly")); //
    }

    @Test
    public void testSetNewIssuesOnly() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        boolean newIssuesOnly = true;
        Assert.assertNotSame(newIssuesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertNull(invokeGetter(p, "scoreConfig")); //
        invokeSetter(p, "newIssuesOnly", newIssuesOnly);
        Assert.assertEquals(newIssuesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        newIssuesOnly = false;
        Assert.assertNotSame(newIssuesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        invokeSetter(p, "postScore", true);
        invokeSetter(p, "newIssuesOnly", newIssuesOnly);
        Assert.assertEquals(newIssuesOnly, invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertEquals(newIssuesOnly, invokeGetter(p, "scoreConfig", "issueFilterConfig", "newIssuesOnly")); //
    }

    @Test
    public void testSetNoIssuesToPostText() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String noIssuesToPostText = "Test";
        Assert.assertNotSame(noIssuesToPostText, invokeGetter(p, "reviewConfig", "noIssuesTitleTemplate"));
        invokeSetter(p, "noIssuesToPostText", noIssuesToPostText);
        Assert.assertEquals(noIssuesToPostText, invokeGetter(p, "reviewConfig", "noIssuesTitleTemplate"));
    }

    @Test
    public void testSetSomeIssuesToPostText() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String someIssuesToPostText = "Test";
        Assert.assertNotSame(someIssuesToPostText, invokeGetter(p, "reviewConfig", "someIssuesTitleTemplate"));
        invokeSetter(p, "someIssuesToPostText", someIssuesToPostText);
        Assert.assertEquals(someIssuesToPostText, invokeGetter(p, "reviewConfig", "someIssuesTitleTemplate"));
    }

    @Test
    public void testSetIssueComment() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String issueComment = "Test";
        Assert.assertNotSame(issueComment, invokeGetter(p, "reviewConfig", "issueCommentTemplate"));
        invokeSetter(p, "issueComment", issueComment);
        Assert.assertEquals(issueComment, invokeGetter(p, "reviewConfig", "issueCommentTemplate"));
    }

    @Test
    public void testSetOverrideCredentials() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        boolean overrideCredentials = true;
        Assert.assertNull(invokeGetter(p, "authConfig"));
        invokeSetter(p, "overrideCredentials", overrideCredentials);
        Assert.assertNotNull(invokeGetter(p, "authConfig"));

        // todo check false
    }

    @Test
    public void testSetHttpUsername() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String httpUsername = "Test";
        Assert.assertNull(invokeGetter(p, "authConfig"));
        invokeSetter(p, "httpUsername", httpUsername);
        Assert.assertNull(invokeGetter(p, "authConfig"));

        invokeSetter(p, "overrideCredentials", true);
        invokeSetter(p, "httpUsername", httpUsername);
        Assert.assertNotNull(invokeGetter(p, "authConfig"));
        Assert.assertEquals(httpUsername, invokeGetter(p, "authConfig", "username"));
    }

    @Test
    public void testSetHttpPassword() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String httpPassword = "Test";
        Assert.assertNull(invokeGetter(p, "authConfig"));
        invokeSetter(p, "httpPassword", httpPassword);
        Assert.assertNull(invokeGetter(p, "authConfig"));

        invokeSetter(p, "overrideCredentials", true);
        invokeSetter(p, "httpPassword", httpPassword);
        Assert.assertNotNull(invokeGetter(p, "authConfig"));
        Assert.assertEquals(httpPassword, invokeGetter(p, "authConfig", "password"));
    }

    @Test
    public void testSetPostScore() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        boolean postScore = true;
        Assert.assertNull(invokeGetter(p, "scoreConfig"));
        invokeSetter(p, "postScore", postScore);
        Assert.assertNotNull(invokeGetter(p, "scoreConfig"));
    }

    @Test
    public void testSetCategory() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String category = "Test";
        Assert.assertNull(invokeGetter(p, "scoreConfig"));
        invokeSetter(p, "category", category);
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        invokeSetter(p, "postScore", true);
        invokeSetter(p, "category", category);
        Assert.assertNotNull(invokeGetter(p, "scoreConfig"));
        Assert.assertEquals(category, invokeGetter(p, "scoreConfig", "category"));
    }

    @Test
    public void testSetNoIssuesScore() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String noIssuesScore = "2";
        Assert.assertNull(invokeGetter(p, "scoreConfig"));
        invokeSetter(p, "noIssuesScore", noIssuesScore);
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        invokeSetter(p, "postScore", true);
        invokeSetter(p, "noIssuesScore", noIssuesScore);
        Assert.assertNotNull(invokeGetter(p, "scoreConfig"));
        Assert.assertEquals(noIssuesScore, invokeGetter(p, "scoreConfig", "noIssuesScore").toString());
    }

    @Test
    public void testSetIssuesScore() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String issuesScore = "-2";
        Assert.assertNull(invokeGetter(p, "scoreConfig"));
        invokeSetter(p, "issuesScore", issuesScore);
        Assert.assertNull(invokeGetter(p, "scoreConfig"));

        invokeSetter(p, "postScore", true);
        invokeSetter(p, "issuesScore", issuesScore);
        Assert.assertNotNull(invokeGetter(p, "scoreConfig"));
        Assert.assertEquals(issuesScore, invokeGetter(p, "scoreConfig", "issuesScore").toString());
    }

    @Test
    public void testSetNoIssuesNotification() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String noIssuesNotification = "ALL";
        Assert.assertNotSame(noIssuesNotification, invokeGetter(p, "notificationConfig", "noIssuesNotificationRecipient"));
        invokeSetter(p, "noIssuesNotification", noIssuesNotification);
        Assert.assertEquals(noIssuesNotification, invokeGetter(p, "notificationConfig", "noIssuesNotificationRecipient"));
    }

    @Test
    public void testSetIssuesNotification() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String issuesNotification = "ALL";
        Assert.assertNotSame(issuesNotification, invokeGetter(p, "notificationConfig", "commentedIssuesNotificationRecipient"));
        invokeSetter(p, "issuesNotification", issuesNotification);
        Assert.assertEquals(issuesNotification, invokeGetter(p, "notificationConfig", "commentedIssuesNotificationRecipient"));
    }

    @Test
    public void testSetProjectPath() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String path = "Test";
        Assert.assertNotSame(path, invokeGetter(p, "inspectionConfig", "baseConfig", "projectPath"));
        invokeSetter(p, "projectPath", path);
        Assert.assertEquals(path, invokeGetter(p, "inspectionConfig", "baseConfig", "projectPath"));
    }

    @Test
    public void testSetPath() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String path = "Test";
        Assert.assertNotSame(path, invokeGetter(p, "inspectionConfig", "baseConfig", "klocworkReportPath"));
        invokeSetter(p, "path", path);
        Assert.assertEquals(path, invokeGetter(p, "inspectionConfig", "baseConfig", "klocworkReportPath"));
    }

    @Test
    public void testSetKlocworkURL() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();
        String klocworkURL = "test";
        Assert.assertNotSame(klocworkURL, invokeGetter(p, "inspectionConfig", "serverURL"));
        invokeSetter(p, "klocworkURL", "test");
        Assert.assertEquals("test", invokeGetter(p, "inspectionConfig", "serverURL"));
    }

    @Test
    public void testSubJobConfig() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();

        String className = "org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig";
        String paramsType = "java.lang.String";
        String[] paramClasses = {paramsType, paramsType};
        String[] params = {"TEST", "TEST"};

        Object c = invokeConstructor(className, paramClasses, params);
        Assert.assertNotSame(invokeGetter(c, "projectPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "projectPath"));
        Assert.assertNotSame(invokeGetter(c, "klocworkReportPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "klocworkReportPath"));

        List value = new LinkedList<>();
        value.add(c);
        invokeSetter(p, "subJobConfigs", value);
        Assert.assertEquals(invokeGetter(c, "projectPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "projectPath"));
        Assert.assertEquals(invokeGetter(c, "klocworkReportPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "klocworkReportPath"));

    }

    @Override
    protected void invokeSetter(KlocworkToGerritPublisher obj, String field, Object value) throws ReflectiveOperationException {
        super.invokeSetter(obj, field, value, true);
    }
}
