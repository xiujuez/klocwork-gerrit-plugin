package org.jenkinsci.plugins.klocworkgerrit.review.notification;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class ReviewCommentAndScoreNotificationTest extends ReviewNegativeScoreNotificationTest {
    @Override
    public void initialize() {
        super.initialize();
        commentIssues.put("juice-bootstrap/src/main/java/com/turquoise/juice/bootstrap/plugins/PluginsManager.java", new DummyIssue());
    }
}
