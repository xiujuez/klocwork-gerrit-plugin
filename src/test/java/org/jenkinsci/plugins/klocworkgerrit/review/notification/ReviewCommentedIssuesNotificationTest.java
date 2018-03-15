package org.jenkinsci.plugins.klocworkgerrit.review.notification;

import com.google.gerrit.extensions.api.changes.NotifyHandling;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.config.NotificationConfig;
import org.junit.Assert;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class ReviewCommentedIssuesNotificationTest extends BaseNotificationTest {
    @Override
    public void initialize() {
        super.initialize();
        commentIssues.put("code/core/arp/src/arp_pickup.cpp", new DummyIssue());
    }

    protected NotifyHandling getDefault() {
        return NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_COMMENTED_ISSUES;
    }

    protected void testNotification(NotifyHandling handling, NotifyHandling other) {
        publisher.getNotificationConfig().setNoIssuesNotificationRecipient(other.name());
        publisher.getNotificationConfig().setCommentedIssuesNotificationRecipient(handling.name());
        publisher.getNotificationConfig().setNegativeScoreNotificationRecipient(other.name());
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(handling, reviewResult.notify);
    }
}
