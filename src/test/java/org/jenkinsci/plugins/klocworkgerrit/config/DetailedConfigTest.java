package org.jenkinsci.plugins.klocworkgerrit.config;

import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public interface DetailedConfigTest extends BaseConfigTest {
    String NO_ISSUES_TITLE_TEMPLATE = ReviewConfig.DescriptorImpl.NO_ISSUES_TITLE_TEMPLATE;
    Integer NO_ISSUES_SCORE = ScoreConfig.DescriptorImpl.NO_ISSUES_SCORE;
    Integer SOME_ISSUES_SCORE = ScoreConfig.DescriptorImpl.SOME_ISSUES_SCORE;
    String ISSUE_COMMENT_TEMPLATE = ReviewConfig.DescriptorImpl.ISSUE_COMMENT_TEMPLATE;
    String SOME_ISSUES_TITLE_TEMPLATE = ReviewConfig.DescriptorImpl.SOME_ISSUES_TITLE_TEMPLATE;
    String CATEGORY = ScoreConfig.DescriptorImpl.CATEGORY;
    String NO_ISSUES_NOTIFICATION = NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_NO_ISSUES.name();
    String ISSUES_NOTIFICATION = NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_COMMENTED_ISSUES.name();
    String SCORE_NOTIFICATION = NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_NEGATIVE_SCORE.name();
    String KLOCWORK_URL = KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_URL;
    String KLOCWORK_REPORT_PATH = KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_REPORT_PATH;
    String PROJECT_PATH = KlocworkToGerritPublisher.DescriptorImpl.PROJECT_PATH;
    String SEVERITY = IssueFilterConfig.DescriptorImpl.SEVERITY;
    String DEFAULT_INSPECTION_CONFIG_TYPE = InspectionConfig.DescriptorImpl.DEFAULT_INSPECTION_CONFIG_TYPE;
    boolean NEW_ISSUES_ONLY = IssueFilterConfig.DescriptorImpl.NEW_ISSUES_ONLY;
    boolean CHANGED_LINES_ONLY = IssueFilterConfig.DescriptorImpl.CHANGED_LINES_ONLY;
    boolean PATH_AUTO_MATCH = InspectionConfig.DescriptorImpl.AUTO_MATCH;

    // IssueFilterConfig
    void testSeverity();

    void testNewOnly();

    void testChangedLinesOnly();

    // ReviewConfig

    void testNoIssuesTitleTemplate();

    void testSomeIssuesTitleTemplate();

    void testIssuesCommentTemplate();

    // ScoreConfig

    void testCategory();

    void testNoIssuesScoreScore();

    void testSomeIssuesScoreScore();

    // NotificationConfig

    void testNoIssuesNotificationRecipient();

    void testIssuesNotificationRecipient();

    void testNegativeScoreNotificationRecipient();

    // Klocwork config

    void testSubJobConfig();

    void testKlocworkUrl();

    void testKlocworkReportPath();

    void testProjectConfig();

}
