package org.jenkinsci.plugins.klocworkgerrit.review;

import com.google.gerrit.extensions.api.changes.ReviewInput;
import org.jenkinsci.plugins.klocworkgerrit.config.ScoreConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public class ReviewWithCommentAndScoreIssues extends ReviewResultTest implements GerritReviewTest {
    @Before
    public void initialize() {
        super.initialize();
        commentIssues.put("juice-bootstrap/src/main/java/com/turquoise/juice/bootstrap/plugins/PluginsManager.java", new DummyIssue());
        commentIssues.put("juice-bootstrap/src/main/java/com/turquoise/juice/bootstrap/plugins/PluginsManager1.java", new DummyIssue());
        scoreIssues.put("juice-bootstrap/src/main/java/com/turquoise/juice/bootstrap/plugins/PluginsManager1.java", new DummyIssue());
    }

    @Test
    public void testReviewHeader() {
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals("2 Klocwork violations have been found.", reviewResult.message);
    }

    @Override
    public void testOverrideReviewHeader() {
        getReviewConfig().setSomeIssuesTitleTemplate("Some Issues Header");
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals("Some Issues Header", reviewResult.message);
    }

    @Test
    public void testReviewComment() {
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(2, reviewResult.comments.size());
// todo check comment >?        Assert.assertEquals("", reviewResult.robotComments.get(0).);
    }

    @Override
    public void testOverrideReviewComment() {
        getReviewConfig().setIssueCommentTemplate("That's an Issue!");
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(2, reviewResult.comments.size());
        // todo check text
    }

    @Test
    public void testScore() {
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(-1, reviewResult.labels.get(ScoreConfig.DescriptorImpl.CATEGORY).intValue());
    }

    @Test
    public void testOverrideScore() {
        publisher.getScoreConfig().setIssuesScore(-2);
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertEquals(-2, reviewResult.labels.get(CATEGORY).intValue());
    }

    @Override
    public void testCategory() {
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertNotNull(reviewResult.labels.get(CATEGORY));
        Assert.assertEquals(-1, reviewResult.labels.get(CATEGORY).intValue());
    }

    @Test
    public void testOverrideCategory() {
        publisher.getScoreConfig().setCategory("Other");
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertNull(reviewResult.labels.get(ScoreConfig.DescriptorImpl.CATEGORY));
        Assert.assertEquals(-1, reviewResult.labels.get("Other").intValue());
    }

    @Override
    public void testOverrideScoreAndCategory() {
        publisher.getScoreConfig().setCategory("Other");
        publisher.getScoreConfig().setIssuesScore(-2);
        ReviewInput reviewResult = getReviewResult();
        Assert.assertEquals(1, reviewResult.labels.size());
        Assert.assertNull(reviewResult.labels.get(CATEGORY));
        Assert.assertNotNull(reviewResult.labels.get("Other"));
        Assert.assertEquals(-2, reviewResult.labels.get("Other").intValue());
    }

}
