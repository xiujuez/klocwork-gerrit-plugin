package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.BaseFilterTest;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * <p>
 * $Id$
 */
public abstract class FilterSeverityTest extends BaseFilterTest<String> {

    @Test
    public void testReviewSeverity() {
        doCheckSeverity(Severity.Review, 10);
    }

    @Test
    public void testWarningSeverity() {
        doCheckSeverity(Severity.Warning, 6);
    }

    @Test
    public void testErrorSeverity() {
        doCheckSeverity(Severity.Error, 5);
    }

    @Test
    public void testCriticalSeverity() {
        doCheckSeverity(Severity.Critical, 4);
    }

    @Override
    public void setFilter(String severity) {
        setSeverity(getFilterConfig(), severity);
        Assert.assertEquals(severity, getFilterConfig().getSeverity());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        setFilter(IssueFilterConfig.DescriptorImpl.SEVERITY);
    }

    @Override
    protected void doCheckFilteredOutByCriteria(String severityStr) {
        Severity severity = Severity.valueOf(severityStr);
        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isSeverityCriteriaSatisfied(severity, issue));
            }
        }
    }

    private void doCheckSeverity(Severity severity, int expectedCount) {
        setFilter(severity.name());
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckSeverity(severity);
        doCheckFilteredOutByCriteria(severity.name());
    }

    protected abstract IssueFilterConfig getFilterConfig();


}
