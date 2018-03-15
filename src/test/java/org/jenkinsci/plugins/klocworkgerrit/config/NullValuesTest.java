package org.jenkinsci.plugins.klocworkgerrit.config;

import junit.framework.Assert;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class NullValuesTest implements DetailedConfigTest {

    @Test
    public void testSeverity() {
        IssueFilterConfig config = new IssueFilterConfig();
        config.setSeverity(null);
        Assert.assertEquals(SEVERITY, config.getSeverity());
    }

    public void testNewOnly() {
        // boolean doesn't allow null
    }

    public void testChangedLinesOnly() {
        // boolean doesn't allow null
    }

    @Test
    public void testFilterConfig() {
        ReviewConfig config = new ReviewConfig();
        config.setIssueFilterConfig(null);
        Assert.assertNotNull(config.getIssueFilterConfig());
        Assert.assertEquals(SEVERITY, config.getIssueFilterConfig().getSeverity());
        Assert.assertEquals(NEW_ISSUES_ONLY, config.getIssueFilterConfig().isNewIssuesOnly());
        Assert.assertEquals(CHANGED_LINES_ONLY, config.getIssueFilterConfig().isChangedLinesOnly());

        IssueFilterConfig fconfig = new IssueFilterConfig(null, false, false);
        Assert.assertEquals(SEVERITY, fconfig.getSeverity());
    }

    @Test
    public void testNoIssuesTitleTemplate() {
        ReviewConfig config = new ReviewConfig();
        config.setNoIssuesTitleTemplate(null);
        Assert.assertEquals(NO_ISSUES_TITLE_TEMPLATE, config.getNoIssuesTitleTemplate());
    }

    @Test
    public void testSomeIssuesTitleTemplate() {
        ReviewConfig config = new ReviewConfig();
        config.setSomeIssuesTitleTemplate(null);
        Assert.assertEquals(SOME_ISSUES_TITLE_TEMPLATE, config.getSomeIssuesTitleTemplate());
    }

    @Override
    public void testIssuesCommentTemplate() {
        ReviewConfig config = new ReviewConfig();
        config.setIssueCommentTemplate(null);
        Assert.assertEquals(ISSUE_COMMENT_TEMPLATE, config.getIssueCommentTemplate());
    }

    @Test
    public void testReviewConfig() {
        KlocworkToGerritPublisher publisher = new KlocworkToGerritPublisher();
        publisher.setReviewConfig(null);
        ReviewConfig config = publisher.getReviewConfig();
        Assert.assertNotNull(config);
        Assert.assertEquals(NO_ISSUES_TITLE_TEMPLATE, config.getNoIssuesTitleTemplate());
        Assert.assertEquals(SOME_ISSUES_TITLE_TEMPLATE, config.getSomeIssuesTitleTemplate());
        Assert.assertEquals(ISSUE_COMMENT_TEMPLATE, config.getIssueCommentTemplate());
        Assert.assertEquals(SEVERITY, config.getIssueFilterConfig().getSeverity());
        Assert.assertEquals(NEW_ISSUES_ONLY, config.getIssueFilterConfig().isNewIssuesOnly());
        Assert.assertEquals(CHANGED_LINES_ONLY, config.getIssueFilterConfig().isChangedLinesOnly());

        config = new ReviewConfig(null, null, null, null);
        Assert.assertNotNull(config.getIssueFilterConfig());
        Assert.assertEquals(SEVERITY, config.getIssueFilterConfig().getSeverity());
        Assert.assertEquals(NEW_ISSUES_ONLY, config.getIssueFilterConfig().isNewIssuesOnly());
        Assert.assertEquals(CHANGED_LINES_ONLY, config.getIssueFilterConfig().isChangedLinesOnly());
        Assert.assertEquals(NO_ISSUES_TITLE_TEMPLATE, config.getNoIssuesTitleTemplate());
        Assert.assertEquals(SOME_ISSUES_TITLE_TEMPLATE, config.getSomeIssuesTitleTemplate());
        Assert.assertEquals(ISSUE_COMMENT_TEMPLATE, config.getIssueCommentTemplate());
    }

    @Test
    public void testCategory() {
        ScoreConfig config = new ScoreConfig();
        config.setCategory(null);
        Assert.assertEquals(CATEGORY, config.getCategory());
    }

    @Test
    public void testNoIssuesScoreScore() {
        ScoreConfig config = new ScoreConfig();
        config.setNoIssuesScore(null);
        Assert.assertEquals(NO_ISSUES_SCORE, config.getNoIssuesScore());
    }

    @Test
    public void testSomeIssuesScoreScore() {
        ScoreConfig config = new ScoreConfig();
        config.setIssuesScore(null);
        Assert.assertEquals(SOME_ISSUES_SCORE, config.getIssuesScore());
    }

    @Test
    public void testScoreConfig() {
        KlocworkToGerritPublisher publisher = new KlocworkToGerritPublisher();
        publisher.setScoreConfig(null);
        Assert.assertNull(publisher.getScoreConfig());

        ScoreConfig config = new ScoreConfig(null, null, null, null);
        Assert.assertNotNull(config.getIssueFilterConfig());
        Assert.assertEquals(SEVERITY, config.getIssueFilterConfig().getSeverity());
        Assert.assertEquals(NEW_ISSUES_ONLY, config.getIssueFilterConfig().isNewIssuesOnly());
        Assert.assertEquals(CHANGED_LINES_ONLY, config.getIssueFilterConfig().isChangedLinesOnly());
        Assert.assertEquals(CATEGORY, config.getCategory());
        Assert.assertEquals(NO_ISSUES_SCORE, config.getNoIssuesScore());
        Assert.assertEquals(SOME_ISSUES_SCORE, config.getIssuesScore());

    }

    @Test
    public void testNoIssuesNotificationRecipient() {
        NotificationConfig config = new NotificationConfig();
        config.setNoIssuesNotificationRecipient(null);
        Assert.assertEquals(NO_ISSUES_NOTIFICATION, config.getNoIssuesNotificationRecipient());
    }

    @Test
    public void testIssuesNotificationRecipient() {
        NotificationConfig config = new NotificationConfig();
        config.setCommentedIssuesNotificationRecipient(null);
        Assert.assertEquals(ISSUES_NOTIFICATION, config.getCommentedIssuesNotificationRecipient());
    }

    @Test
    public void testNegativeScoreNotificationRecipient() {
        NotificationConfig config = new NotificationConfig();
        config.setNegativeScoreNotificationRecipient(null);
        Assert.assertEquals(SCORE_NOTIFICATION, config.getNegativeScoreNotificationRecipient());
    }

    @Test
    public void testNotificationConfig() {
        KlocworkToGerritPublisher publisher = new KlocworkToGerritPublisher();
        publisher.setNotificationConfig(null);
        NotificationConfig config = publisher.getNotificationConfig();
        Assert.assertNotNull(config);
        Assert.assertEquals(NO_ISSUES_NOTIFICATION, config.getNoIssuesNotificationRecipient());
        Assert.assertEquals(ISSUES_NOTIFICATION, config.getCommentedIssuesNotificationRecipient());
        Assert.assertEquals(SCORE_NOTIFICATION, config.getNegativeScoreNotificationRecipient());

        config = new NotificationConfig(null, null, "");
        Assert.assertEquals(NO_ISSUES_NOTIFICATION, config.getNoIssuesNotificationRecipient());
        Assert.assertEquals(ISSUES_NOTIFICATION, config.getCommentedIssuesNotificationRecipient());
        Assert.assertEquals(SCORE_NOTIFICATION, config.getNegativeScoreNotificationRecipient());

    }

    @Test
    public void testKlocworkUrl() {
        InspectionConfig config = new InspectionConfig();
        config.setServerURL(null);
        Assert.assertEquals(KLOCWORK_URL, config.getServerURL());
    }

    @Test
    public void testKlocworkReportPath() {
        SubJobConfig config = new SubJobConfig();
        Assert.assertEquals(KLOCWORK_REPORT_PATH, config.getKlocworkReportPath());
        config.setKlocworkReportPath(null);
        Assert.assertEquals(KLOCWORK_REPORT_PATH, config.getKlocworkReportPath());
    }

    @Test
    public void testInspectionConfig() {
        KlocworkToGerritPublisher publisher = new KlocworkToGerritPublisher();
        publisher.setInspectionConfig(null);
        InspectionConfig config = publisher.getInspectionConfig();
        Assert.assertNotNull(config);
        Assert.assertEquals(KLOCWORK_URL, config.getServerURL());
        Assert.assertEquals(KLOCWORK_REPORT_PATH, config.getBaseConfig().getKlocworkReportPath());
        Assert.assertEquals(PROJECT_PATH, config.getBaseConfig().getProjectPath());
        Assert.assertEquals(PATH_AUTO_MATCH, config.getBaseConfig().isAutoMatch());
        Assert.assertNotNull(config.getSubJobConfigs());
        Assert.assertEquals(1, config.getSubJobConfigs().size());
        SubJobConfig subJobConfig = new ArrayList<>(config.getAllSubJobConfigs()).get(0);
        Assert.assertEquals(KLOCWORK_REPORT_PATH, subJobConfig.getKlocworkReportPath());
        Assert.assertEquals(PROJECT_PATH, subJobConfig.getProjectPath());
        Assert.assertFalse(subJobConfig.isAutoMatch());
    }

    @Test
    public void testSubJobConfig() {
        InspectionConfig config = new InspectionConfig();
        config.setBaseConfig(null);
        Assert.assertNotNull(config.getBaseConfig());
        Assert.assertEquals(KLOCWORK_REPORT_PATH, config.getBaseConfig().getKlocworkReportPath());
        Assert.assertEquals(PROJECT_PATH, config.getBaseConfig().getProjectPath());
        Assert.assertEquals(PATH_AUTO_MATCH, config.getBaseConfig().isAutoMatch());

        Assert.assertNotNull(config.getSubJobConfigs());
        Assert.assertEquals(1, config.getSubJobConfigs().size());
        SubJobConfig subJobConfig = new ArrayList<>(config.getSubJobConfigs()).get(0);
        Assert.assertEquals(KLOCWORK_REPORT_PATH, subJobConfig.getKlocworkReportPath());
        Assert.assertEquals(PROJECT_PATH, subJobConfig.getProjectPath());
        Assert.assertFalse(subJobConfig.isAutoMatch());
    }

    @Test
    public void testProjectConfig() {
        SubJobConfig config = new SubJobConfig();
        Assert.assertEquals(PROJECT_PATH, config.getProjectPath());
        config.setProjectPath(null);
        Assert.assertEquals(PROJECT_PATH, config.getProjectPath());
    }

    //@Test
    public void testAuthenticationConfig() {

    }
}
