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
public class ReviewNoIssuesNotificationTest extends BaseNotificationTest{
    protected NotifyHandling getDefault() {
        return NotificationConfig.DescriptorImpl.NOTIFICATION_RECIPIENT_NO_ISSUES;
    }

    protected void testNotification(NotifyHandling handling, NotifyHandling other) {
        publisher.getNotificationConfig().setNoIssuesNotificationRecipient(handling.name());
        publisher.getNotificationConfig().setCommentedIssuesNotificationRecipient(other.name());
        publisher.getNotificationConfig().setNegativeScoreNotificationRecipient(other.name());
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(handling, reviewResult.notify);
    }
}
