package org.jenkinsci.plugins.klocworkgerrit.filter;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.BaseFilterTest;
import org.jenkinsci.plugins.klocworkgerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.klocworkgerrit.filter.util.Triple;
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
public abstract class FilterSeverityNewChangedOnly extends BaseFilterTest<Triple<String, Boolean, Boolean>> {

    @Test
    public void testReviewSeverity() {
        doCheckSeverityNewAndChanged(Severity.Review, true, true, 4);
        doCheckSeverityNewAndChanged(Severity.Review, false, true, 4);
        doCheckSeverityNewAndChanged(Severity.Review, true, false, 5);
        doCheckSeverityNewAndChanged(Severity.Review, false, false, 10);
    }

    @Test
    public void testWarningSeverity() {
        doCheckSeverityNewAndChanged(Severity.Warning, true, true, 1);
        doCheckSeverityNewAndChanged(Severity.Warning, false, true, 1);
        doCheckSeverityNewAndChanged(Severity.Warning, true, false, 2);
        doCheckSeverityNewAndChanged(Severity.Warning, false, false, 6);
    }

    @Test
    public void testErrorSeverity() {
        doCheckSeverityNewAndChanged(Severity.Error, true, true, 1);
        doCheckSeverityNewAndChanged(Severity.Error, false, true, 1);
        doCheckSeverityNewAndChanged(Severity.Error, true, false, 2);
        doCheckSeverityNewAndChanged(Severity.Error, false, false, 5);
    }

    @Test
    public void testCriticalSeverity() {
        doCheckSeverityNewAndChanged(Severity.Critical, true, true, 0);
        doCheckSeverityNewAndChanged(Severity.Critical, false, true, 0);
        doCheckSeverityNewAndChanged(Severity.Critical, true, false, 1);
        doCheckSeverityNewAndChanged(Severity.Critical, false, false, 4);
    }

    @Override
    public void setFilter(Triple<String, Boolean, Boolean> severityNewChanged) {
        String severity = severityNewChanged.getFirst();
        setSeverity(getFilterConfig(), severity);
        Assert.assertEquals(severity, getFilterConfig().getSeverity());

        Boolean newOnly = severityNewChanged.getSecond();
        setNewOnly(getFilterConfig(), newOnly);
        Assert.assertEquals(newOnly, getFilterConfig().isNewIssuesOnly());

        Boolean changedOnly = severityNewChanged.getThird();
        setChangedOnly(getFilterConfig(), changedOnly);
        Assert.assertEquals(changedOnly, getFilterConfig().isChangedLinesOnly());
    }

    @Override
    public void resetFilter() {
        super.resetFilter();
        String severity = IssueFilterConfig.DescriptorImpl.SEVERITY;
        Boolean newOnly = IssueFilterConfig.DescriptorImpl.NEW_ISSUES_ONLY;
        Boolean changedOnly = IssueFilterConfig.DescriptorImpl.CHANGED_LINES_ONLY;
        setFilter(new Triple<String, Boolean, Boolean>(severity, newOnly, changedOnly));
    }

    @Override
    protected void doCheckFilteredOutByCriteria(Triple<String, Boolean, Boolean> severityNewChanged) {
        Severity severity = Severity.valueOf(severityNewChanged.getFirst());
        Boolean newOnly = severityNewChanged.getSecond();
        Boolean changedOnly = severityNewChanged.getThird();

        // check that all filtered out issues have severity lower than criteria
        for (IssueAdapter issue : filteredOutIssues) {
            if (isFileChanged(issue)) {
                Assert.assertFalse(isSeverityCriteriaSatisfied(severity, issue)
                        && isChangedLinesOnlyCriteriaSatisfied(changedOnly, issue)
                        && isNewOnlyCriteriaSatisfied(newOnly, issue));
            }
        }
    }

    private void doCheckSeverityNewAndChanged(Severity severity, boolean newOnly, boolean changedLinesOnly, int expectedCount) {
        Triple<String, Boolean, Boolean> severityNewChanged = new Triple<String, Boolean, Boolean>(severity.name(), newOnly, changedLinesOnly);
        setFilter(severityNewChanged);
        doFilterIssues(getFilterConfig());

        doCheckCount(expectedCount);
        doCheckSeverity(severity);
        doCheckNewOnly(newOnly);
        doCheckChangedLinesOnly(changedLinesOnly);
        doCheckFilteredOutByCriteria(severityNewChanged);
    }

    protected abstract IssueFilterConfig getFilterConfig();

}
