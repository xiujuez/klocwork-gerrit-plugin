package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public class CommentFilterNewOnlyTest extends FilterNewOnlyTest {

    @Override
    protected IssueFilterConfig getFilterConfig(){
        return publisher.getReviewConfig().getIssueFilterConfig();
    }
}
