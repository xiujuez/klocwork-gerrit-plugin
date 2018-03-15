package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.BaseFilterTest;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public abstract class FilterNewOnlyTest extends BaseFilterTest<Boolean> {
    @Test
    public void testNewOnly() {
        doCheckNewOnly(true, 5);
    }

    @Test
    public void testAll() {
        doCheckNewOnly(false, 10);
    }

    @Override
    public void setFilter(Boolean newOnly) {
        setNewOnly(getFilterConfig(), newOnly);
        Assert.assertEquals(newOnly, getFilterConfig().isNewIssuesOnly());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        setFilter(IssueFilterConfig.DescriptorImpl.NEW_ISSUES_ONLY);
    }

    @Override
    protected void doCheckFilteredOutByCriteria(Boolean newOnly) {
        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isNewOnlyCriteriaSatisfied(newOnly, issue));
            }
        }
    }

    private void doCheckNewOnly(Boolean newOnly, int expectedCount) {
        setFilter(newOnly);
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckNewOnly(newOnly);
        doCheckFilteredOutByCriteria(newOnly);
    }

}

