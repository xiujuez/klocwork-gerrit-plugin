package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.BaseFilterTest;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.filter.util.Pair;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.junit.Assert;
import org.junit.Test;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * $Id$
 */
public abstract class FilterSeverityAndChangedLinesOnly extends BaseFilterTest<Pair<String, Boolean>> {

    @Test
    public void testReviewSeverity() {
        doCheckSeverityAndChanged(Severity.Review, true, 4);
        doCheckSeverityAndChanged(Severity.Review, false, 10);
    }

    @Test
    public void testWarningSeverity() {
        doCheckSeverityAndChanged(Severity.Warning, true, 1);
        doCheckSeverityAndChanged(Severity.Warning, false, 6);
    }

    @Test
    public void testErrorSeverity() {
        doCheckSeverityAndChanged(Severity.Error, true, 1);
        doCheckSeverityAndChanged(Severity.Error, false, 5);
    }

    @Test
    public void testCriticalSeverity() {
        doCheckSeverityAndChanged(Severity.Critical, true, 0);
        doCheckSeverityAndChanged(Severity.Critical, false, 4);
    }

    @Override
    public void setFilter(Pair<String, Boolean> severityAndChanged) {
        String severity = severityAndChanged.getFirst();
        setSeverity(getFilterConfig(), severity);
        Assert.assertEquals(severity, getFilterConfig().getSeverity());

        Boolean changedOnly = severityAndChanged.getSecond();
        setChangedOnly(getFilterConfig(), changedOnly);
        Assert.assertEquals(changedOnly, getFilterConfig().isChangedLinesOnly());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        String severity = IssueFilterConfig.DescriptorImpl.SEVERITY;
        Boolean changedOnly = IssueFilterConfig.DescriptorImpl.CHANGED_LINES_ONLY;
        setFilter(new Pair<String, Boolean>(severity, changedOnly));
    }

    @Override
    protected void doCheckFilteredOutByCriteria(Pair<String, Boolean> severityAndChanged) {
        Severity severity = Severity.valueOf(severityAndChanged.getFirst());
        Boolean changedOnly = severityAndChanged.getSecond();

        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isSeverityCriteriaSatisfied(severity, issue) && isChangedLinesOnlyCriteriaSatisfied(changedOnly, issue));
            }
        }
    }

    private void doCheckSeverityAndChanged(Severity severity, boolean changedLinesOnly, int expectedCount) {
        Pair<String, Boolean> severityAndChanged = new Pair<String, Boolean>(severity.name(), changedLinesOnly);
        setFilter(severityAndChanged);
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckSeverity(severity);
        doCheckChangedLinesOnly(changedLinesOnly);
        doCheckFilteredOutByCriteria(severityAndChanged);
    }

    protected abstract IssueFilterConfig getFilterConfig();


}
