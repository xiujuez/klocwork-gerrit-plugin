package org.jenkinsci.plugins.klocworkgerrit.signature;

import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 02.01.2018 11:11
 * $Id$
 * */

/*
 * If this test fails then probably KlocworkToGerritPublisher's signature has changed.
 * In order to save plugin configuration that was set up with old configuration, it
 * is necessary to keep old setter methods, mark them as @Deprecated and move test to
 * BackCompatibilityConfigurationTest
 */

public class UnexpectedMethodSignatureChangeTest extends ConfigurationUpdateTest {

    @Test
    public void testInspectionConfig() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();

        String configClassName = "org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig";
        String stringType = "java.lang.String";
        String[] configParamClasses = {stringType, stringType};
        String[] config1Params = {"TEST", "TEST"};
        String[] config2Params = {"TEST2", "TEST2"};
        String[] config3Params = {"TEST3", "TEST3"};


        Object config1 = invokeConstructor(configClassName, configParamClasses, config1Params);
        Object config2 = invokeConstructor(configClassName, configParamClasses, config2Params);
        Object config3 = invokeConstructor(configClassName, configParamClasses, config3Params);
        Collection configs = Arrays.asList(config2, config3);

        String className = "org.jenkinsci.plugins.klocworkgerrit.config.InspectionConfig";
        String[] paramClasses = {};
        Object[] params = {};

        Object c = invokeConstructor(className, paramClasses, params);
        invokeSetter(p, "inspectionConfig", c);
        invokeSetter(p, config1, false, "inspectionConfig", "baseConfig");
        invokeSetter(p, configs, false, "inspectionConfig", "subJobConfigs");

        Assert.assertEquals("http://localhost:8080", invokeGetter(p, "inspectionConfig", "serverURL"));

        Assert.assertEquals(invokeGetter(config1, "projectPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "projectPath"));
        Assert.assertEquals(invokeGetter(config1, "klocworkReportPath"), invokeGetter(p, "inspectionConfig", "baseConfig", "klocworkReportPath"));

        Assert.assertEquals(invokeGetter(config2, "projectPath"), invokeGetter(p, "inspectionConfig", "subJobConfigs", "projectPath"));
        Assert.assertEquals(invokeGetter(config2, "klocworkReportPath"), invokeGetter(p, "inspectionConfig", "subJobConfigs", "klocworkReportPath"));
    }

    @Test
    public void testSetNotificationConfig() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();

        String className = "org.jenkinsci.plugins.klocworkgerrit.config.NotificationConfig";
        String paramsType = "java.lang.String";
        String[] paramClasses = {paramsType, paramsType, paramsType};
        String[] params = {"ALL", "OWNER_REVIEWERS", "NONE"};

        Object c = invokeConstructor(className, paramClasses, params);
        Assert.assertNotSame(invokeGetter(c, "noIssuesNotificationRecipient"), invokeGetter(p, "notificationConfig", "noIssuesNotificationRecipient"));
        Assert.assertNotSame(invokeGetter(c, "commentedIssuesNotificationRecipient"), invokeGetter(p, "notificationConfig", "commentedIssuesNotificationRecipient"));
        Assert.assertNotSame(invokeGetter(c, "negativeScoreNotificationRecipient"), invokeGetter(p, "notificationConfig", "negativeScoreNotificationRecipient"));

        invokeSetter(p, "notificationConfig", c);
        Assert.assertEquals(invokeGetter(c, "noIssuesNotificationRecipient"), invokeGetter(p, "notificationConfig", "noIssuesNotificationRecipient"));
        Assert.assertEquals(invokeGetter(c, "commentedIssuesNotificationRecipient"), invokeGetter(p, "notificationConfig", "commentedIssuesNotificationRecipient"));
        Assert.assertEquals(invokeGetter(c, "negativeScoreNotificationRecipient"), invokeGetter(p, "notificationConfig", "negativeScoreNotificationRecipient"));
    }

    @Test
    public void testSetReviewConfig() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();

        String className = "org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig";
        String stringType = "java.lang.String";
        String booleanType = "boolean";

        String[] paramClasses = {stringType, booleanType, booleanType};
        Object[] params = {"Critical", true, true};

        Object c1 = invokeConstructor(className, paramClasses, params);
        Assert.assertNotSame(invokeGetter(c1, "severity"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        Assert.assertNotSame(invokeGetter(c1, "newIssuesOnly"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertNotSame(invokeGetter(c1, "changedLinesOnly"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));

        String className2 = "org.jenkinsci.plugins.klocworkgerrit.config.ReviewConfig";

        String[] paramClasses2 = {className, stringType, stringType, stringType};
        Object[] params2 = {c1, "Test", "Test", "Test"};

        Object c2 = invokeConstructor(className2, paramClasses2, params2);
        Assert.assertNotSame(invokeGetter(c2, "noIssuesTitleTemplate"), invokeGetter(p, "reviewConfig", "noIssuesTitleTemplate"));
        Assert.assertNotSame(invokeGetter(c2, "someIssuesTitleTemplate"), invokeGetter(p, "reviewConfig", "someIssuesTitleTemplate"));
        Assert.assertNotSame(invokeGetter(c2, "issueCommentTemplate"), invokeGetter(p, "reviewConfig", "issueCommentTemplate"));

        invokeSetter(p, "reviewConfig", c2);
        Assert.assertEquals(invokeGetter(c1, "severity"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "severity"));
        Assert.assertEquals(invokeGetter(c1, "newIssuesOnly"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertEquals(invokeGetter(c1, "changedLinesOnly"), invokeGetter(p, "reviewConfig", "issueFilterConfig", "changedLinesOnly"));
        Assert.assertEquals(invokeGetter(c2, "noIssuesTitleTemplate"), invokeGetter(p, "reviewConfig", "noIssuesTitleTemplate"));
        Assert.assertEquals(invokeGetter(c2, "someIssuesTitleTemplate"), invokeGetter(p, "reviewConfig", "someIssuesTitleTemplate"));
        Assert.assertEquals(invokeGetter(c2, "issueCommentTemplate"), invokeGetter(p, "reviewConfig", "issueCommentTemplate"));
    }

    @Test
    public void testSetScoreConfig() throws ReflectiveOperationException {
        KlocworkToGerritPublisher p = invokeConstructor();

        String className = "org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig";
        String stringType = "java.lang.String";
        String booleanType = "boolean";
        String integerType = "java.lang.Integer";

        String[] paramClasses = {stringType, booleanType, booleanType};
        Object[] params = {"Critical", true, true};

        Object c1 = invokeConstructor(className, paramClasses, params);
//        Assert.assertNotSame(invokeGetter(c1, "severity"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "severity"));
//        Assert.assertNotSame(invokeGetter(c1, "newIssuesOnly"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "newIssuesOnly"));
//        Assert.assertNotSame(invokeGetter(c1, "changedLinesOnly"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "changedLinesOnly"));

        String className2 = "org.jenkinsci.plugins.klocworkgerrit.config.ScoreConfig";

        String[] paramClasses2 = {className, stringType, integerType, integerType};
        Object[] params2 = {c1, "Test", 2, -2};

        Object c2 = invokeConstructor(className2, paramClasses2, params2);

        Assert.assertNull(invokeGetter(p, "scoreConfig"));
//        Assert.assertNotSame(invokeGetter(c2, "category"), invokeGetter(p, "scoreConfig", "category"));
//        Assert.assertNotSame(invokeGetter(c2, "noIssuesScore"), invokeGetter(p, "scoreConfig", "noIssuesScore"));
//        Assert.assertNotSame(invokeGetter(c2, "issuesScore"), invokeGetter(p, "scoreConfig", "issuesScore"));

        invokeSetter(p, "scoreConfig", c2);
        Assert.assertEquals(invokeGetter(c1, "severity"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "severity"));
        Assert.assertEquals(invokeGetter(c1, "newIssuesOnly"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "newIssuesOnly"));
        Assert.assertEquals(invokeGetter(c1, "changedLinesOnly"), invokeGetter(p, "scoreConfig", "issueFilterConfig", "changedLinesOnly"));
        Assert.assertEquals(invokeGetter(c2, "category"), invokeGetter(p, "scoreConfig", "category"));
        Assert.assertEquals(invokeGetter(c2, "noIssuesScore"), invokeGetter(p, "scoreConfig", "noIssuesScore"));
        Assert.assertEquals(invokeGetter(c2, "issuesScore"), invokeGetter(p, "scoreConfig", "issuesScore"));
    }


}
