package org.jenkinsci.plugins.klocworkgerrit.review;

import org.jenkinsci.plugins.klocworkgerrit.config.ReviewConfig;
import org.jenkinsci.plugins.klocworkgerrit.config.ScoreConfig;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public interface GerritReviewTest {
    String CATEGORY = ScoreConfig.DescriptorImpl.CATEGORY;
    Integer NO_ISSUES_SCORE = ScoreConfig.DescriptorImpl.NO_ISSUES_SCORE;
    Integer SOME_ISSUES_SCORE = ScoreConfig.DescriptorImpl.SOME_ISSUES_SCORE;
    String NO_ISSUES_TITLE_TEMPLATE = ReviewConfig.DescriptorImpl.NO_ISSUES_TITLE_TEMPLATE;
    String SOME_ISSUES_TITLE_TEMPLATE = ReviewConfig.DescriptorImpl.SOME_ISSUES_TITLE_TEMPLATE;

    void testReviewHeader();

    void testOverrideReviewHeader();

    void testReviewComment();

    void testOverrideReviewComment();

    void testScore();

    void testOverrideScore();

    void testCategory();

    void testOverrideCategory();

    void testNoScoreConfig();

    void testOverrideScoreAndCategory();

}
