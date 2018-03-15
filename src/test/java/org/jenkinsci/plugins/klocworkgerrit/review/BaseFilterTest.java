package org.jenkinsci.plugins.klocworkgerrit.review;

import com.google.common.collect.Sets;
import com.google.gerrit.extensions.common.DiffInfo;
import org.jenkinsci.plugins.klocworkgerrit.ReportBasedTest;
import org.jenkinsci.plugins.klocworkgerrit.KlocworkToGerritPublisher;
import org.jenkinsci.plugins.klocworkgerrit.config.*;
import org.jenkinsci.plugins.klocworkgerrit.filter.IssueFilter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork.KlocworkIssueAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public abstract class BaseFilterTest<A> extends ReportBasedTest {
    protected Report report;
    protected KlocworkToGerritPublisher publisher;

    protected Set<IssueAdapter> filteredIssues;
    protected Set<IssueAdapter> filteredOutIssues;

    protected Map<String, DiffInfo> diffInfo;

    @Before
    public void initialize() throws InterruptedException, IOException, URISyntaxException {
        loadReport();
        buildPublisher();  //todo check all issues read correctly?
        diffInfo = readChange("diff_info.json");
    }

    protected void loadReport() throws InterruptedException, IOException, URISyntaxException {
        report = readreport("filter.json");
        Assert.assertEquals(10, report.getIssues().size());
    }

    @After
    public void resetFilter() {
    }

    @After
    public void reset() {
        filteredIssues = null;
        filteredOutIssues = null;
        diffInfo = null;
    }

    //    @Before
    public void setFilter(A a) {

    }

    protected void buildPublisher() {
        publisher = new KlocworkToGerritPublisher();

        publisher.setKlocworkURL(KlocworkToGerritPublisher.DescriptorImpl.KLOCWORK_URL);


//        publisher.setSubJobConfigs(KlocworkToGerritPublisher.DescriptorImpl.JOB_CONFIGS);
//        Assert.assertEquals(1, publisher.getSubJobConfigs().size());

        publisher.setAuthConfig(null);

        publisher.setReviewConfig(new ReviewConfig());
        publisher.setNotificationConfig(new NotificationConfig());
        publisher.setScoreConfig(new ScoreConfig());
    }

    protected void doFilterIssues(IssueFilterConfig config) {
        // filter issues
        List<Issue> allIssues = report.getIssues();
        List<IssueAdapter> allIssuesAdp = new ArrayList<>();
        for (Issue i : allIssues) {
            allIssuesAdp.add(new KlocworkIssueAdapter(i, i.getFile(), "/share/wireline-vdc-jenkins/shared_work_space/c-zxj-kw-master/build/90/"));
        }

        //todo temporary - should be realized in publisher
        // todo check filtered out as unchanged file
//        List<Issue> step2 = allIssues;
        Map<String, Set<Integer>> changed = getChangedLines();

        IssueFilter filter = new IssueFilter(config, allIssuesAdp, changed);
        filteredIssues = Sets.newHashSet(filter.filter());

        // get issues that were filtered out
        filteredOutIssues = new HashSet<>(allIssuesAdp);
        filteredOutIssues.removeAll(filteredIssues);
    }

    protected Map<String, Set<Integer>> getChangedLines() {
        Map<String, Set<Integer>> changed = new HashMap<>();
        if (diffInfo != null) {
            for (String s : diffInfo.keySet()) {
                GerritRevisionWrapper w = null;
                w = new GerritRevisionWrapper(null);
                changed.put(s, w.getChangedLines(diffInfo.get(s)));
            }
        }
        return changed;
    }

    protected void doCheckSeverity(Severity severity) {
        // check that all remaining issues have severity higher or equal to criteria
        for (IssueAdapter issue : filteredIssues) {
            Assert.assertTrue(isSeverityCriteriaSatisfied(severity, issue));
        }
    }

    protected void doCheckNewOnly(boolean isNewOnly) {
        // check that all remaining issues are new
        for (IssueAdapter issue : filteredIssues) {
            Assert.assertTrue(isNewOnlyCriteriaSatisfied(isNewOnly, issue));
        }
    }

    protected void doCheckChangedLinesOnly(boolean isChangesLinesOnly) {
        // check that all remaining issues are in changed lines
        for (IssueAdapter issue : filteredIssues) {
            Assert.assertTrue(isChangedLinesOnlyCriteriaSatisfied(isChangesLinesOnly, issue));
        }
    }

    protected boolean isSeverityCriteriaSatisfied(Severity severity, IssueAdapter issue) {
        return issue.getSeverity().ordinal() >= severity.ordinal();
    }

    protected boolean isNewOnlyCriteriaSatisfied(Boolean isNewOnly, IssueAdapter issue) {
        return !isNewOnly || issue.isNew();
    }

    protected boolean isChangedLinesOnlyCriteriaSatisfied(Boolean isChangesLinesOnly, IssueAdapter issue) {
        return !isChangesLinesOnly || isChanged(issue.getFilepath(), issue.getLine());
    }

    protected boolean isFileChanged(IssueAdapter issue) {
        String filename = issue.getFilepath();
        DiffInfo diffInfo = this.diffInfo.get(filename);
        return diffInfo != null;
    }

    protected boolean isChanged(String filename, int line) {
        DiffInfo diffInfo = this.diffInfo.get(filename);
        if (diffInfo == null) {
            return false;
        }
        int processed = 0;
        for (DiffInfo.ContentEntry contentEntry : diffInfo.content) {
            if (contentEntry.ab != null) {
                processed += contentEntry.ab.size();
                if (processed >= line) {
                    return false;
                }
            } else if (contentEntry.b != null) {
                processed += contentEntry.b.size();
                if (processed >= line) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract void doCheckFilteredOutByCriteria(A a);

    protected void doCheckCount(int expectedFilteredIssuesCount) {
        // check that amount of filtered issues is equal to expected amount
        Assert.assertEquals(expectedFilteredIssuesCount, filteredIssues.size());

        // get amount of issues that are expected to be filtered out and check it
        List<Issue> allIssues = report.getIssues();
        int expectedFilteredOutCount = allIssues.size() - expectedFilteredIssuesCount;
        Assert.assertEquals(expectedFilteredOutCount, filteredOutIssues.size());
    }

    protected abstract IssueFilterConfig getFilterConfig();

    protected void setSeverity(IssueFilterConfig config, String severity) {
        config.setSeverity(severity);
        Assert.assertEquals(severity, config.getSeverity());
    }

    protected void setNewOnly(IssueFilterConfig config, Boolean newOnly) {
        config.setNewIssuesOnly(newOnly);
        Assert.assertEquals(newOnly, config.isNewIssuesOnly());
    }

    protected void setChangedOnly(IssueFilterConfig config, Boolean changedOnly) {
        config.setChangedLinesOnly(changedOnly);
        Assert.assertEquals(changedOnly, config.isChangedLinesOnly());
    }
}
