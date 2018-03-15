package org.jenkinsci.plugins.klocworkgerrit.review.notification;

import com.google.gerrit.extensions.api.changes.NotifyHandling;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.review.ReviewResultTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public abstract class BaseNotificationTest extends ReviewResultTest implements GerritReviewNotificationTest {
    @Test
    public void testDefaultNotification() {
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(getDefault(), reviewResult.notify);
    }

    @Test
    public void testNone() {
        testNotification(NotifyHandling.NONE, NotifyHandling.OWNER);
    }

    @Test
    public void testOwner() {
        testNotification(NotifyHandling.OWNER, NotifyHandling.NONE);
    }

    @Test
    public void testOwnerReviewers() {
        testNotification(NotifyHandling.OWNER_REVIEWERS, NotifyHandling.NONE);
    }


    @Test
    public void testAll() {
        testNotification(NotifyHandling.ALL, NotifyHandling.NONE);
    }

    protected abstract void testNotification(NotifyHandling expected, NotifyHandling other);

    protected abstract NotifyHandling getDefault();
}
