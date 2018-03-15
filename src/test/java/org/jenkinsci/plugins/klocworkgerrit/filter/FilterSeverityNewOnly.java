package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.BaseFilterTest;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.filter.util.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public abstract class FilterSeverityNewOnly extends BaseFilterTest<Pair<String, Boolean>> {

    @Test
    public void testReviewSeverity() {
        doCheckSeverityNew(Severity.Review, true, 5);
        doCheckSeverityNew(Severity.Review, false, 10);
    }

    @Test
    public void testWarningSeverity() {
        doCheckSeverityNew(Severity.Warning, true, 2);
        doCheckSeverityNew(Severity.Warning, false, 6);
    }

    @Test
    public void testErrorSeverity() {
        doCheckSeverityNew(Severity.Error, true, 2);
        doCheckSeverityNew(Severity.Error, false, 5);
    }

    @Test
    public void testCriticalSeverity() {
        doCheckSeverityNew(Severity.Critical, true, 1);
        doCheckSeverityNew(Severity.Critical, false, 4);
    }

    @Override
    public void setFilter(Pair<String, Boolean> severityAndNew) {
        String severity = severityAndNew.getFirst();
        setSeverity(getFilterConfig(), severity);
        Assert.assertEquals(severity, getFilterConfig().getSeverity());

        Boolean newOnly = severityAndNew.getSecond();
        setNewOnly(getFilterConfig(), newOnly);
        Assert.assertEquals(newOnly, getFilterConfig().isNewIssuesOnly());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        String severity = IssueFilterConfig.DescriptorImpl.SEVERITY;
        Boolean newOnly = IssueFilterConfig.DescriptorImpl.NEW_ISSUES_ONLY;
        setFilter(new Pair<String, Boolean>(severity, newOnly));
    }

    @Override
    protected void doCheckFilteredOutByCriteria(Pair<String, Boolean> severityAndNew) {
        Severity severity = Severity.valueOf(severityAndNew.getFirst());
        Boolean newOnly = severityAndNew.getSecond();

        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isSeverityCriteriaSatisfied(severity, issue) && isNewOnlyCriteriaSatisfied(newOnly, issue));
            }
        }
    }

    private void doCheckSeverityNew(Severity severity, boolean newOnly, int expectedCount) {
        Pair<String, Boolean> severityAndNew = new Pair<String, Boolean>(severity.name(), newOnly);
        setFilter(severityAndNew);
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckSeverity(severity);
        doCheckNewOnly(newOnly);
        doCheckFilteredOutByCriteria(severityAndNew);
    }

    protected abstract IssueFilterConfig getFilterConfig();


}
