package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
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
public abstract class FilterChangedLinesOnlyTest extends BaseFilterTest<Boolean> {
    @Test
    public void testChangedLinesOnly() {
        doCheckChangedLinesOnly(true, 4);
    }

    @Test
    public void testAll() {
        doCheckChangedLinesOnly(false, 10);
    }

    @Override
    public void setFilter(Boolean changedOnly) {
        setChangedOnly(getFilterConfig(), changedOnly);
        Assert.assertEquals(changedOnly, getFilterConfig().isChangedLinesOnly());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        setFilter(IssueFilterConfig.DescriptorImpl.CHANGED_LINES_ONLY);
    }

    @Override
    protected void doCheckFilteredOutByCriteria(Boolean changedOnly) {
        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isChangedLinesOnlyCriteriaSatisfied(changedOnly, issue));
            }
        }
    }

    private void doCheckChangedLinesOnly(Boolean changedOnly, int expectedCount) {
        setFilter(changedOnly);
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckChangedLinesOnly(changedOnly);
        doCheckFilteredOutByCriteria(changedOnly);
    }

}

