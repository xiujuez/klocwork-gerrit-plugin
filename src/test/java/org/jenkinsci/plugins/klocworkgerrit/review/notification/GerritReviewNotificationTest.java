package org.jenkinsci.plugins.klocworkgerrit.review.notification;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public interface GerritReviewNotificationTest {
    void testNone();

    void testOwner();

    void testOwnerReviewers();

    void testAll();
}
