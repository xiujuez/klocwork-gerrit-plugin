package org.jenkinsci.plugins.klocworkgerrit.review.notification;

import com.google.gerrit.extensions.api.changes.NotifyHandling;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.config.NotificationConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkIssueAdapter;
import org.junit.Assert;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class ReviewNegativeScoreNotificationTest extends BaseNotificationTest {
    @Override
    public void initialize() {
        super.initialize();
        scoreIssues.put("juice-bootstrap/src/main/java/com/turquoise/juice/bootstrap/plugins/PluginsManager.java", new DummyIssue());
    }

    protected NotifyHandling getDefault() {
        return NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_NEGATIVE_SCORE;
    }

    protected void testNotification(NotifyHandling handling, NotifyHandling other) {
        publisher.getNotificationConfig().setNoIssuesNotificationRecipient(other.name());
        publisher.getNotificationConfig().setCommentedIssuesNotificationRecipient(other.name());
        publisher.getNotificationConfig().setNegativeScoreNotificationRecipient(handling.name());
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(handling, reviewResult.notify);
    }
}
