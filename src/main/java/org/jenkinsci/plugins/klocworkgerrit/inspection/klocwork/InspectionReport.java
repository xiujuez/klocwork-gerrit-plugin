package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * $Id$
 */
public class InspectionReport {
    private List<IssueAdapter> issuesList;
    private List<KlocworkConnector.ReportInfo> reportInfos;

    public InspectionReport(List<KlocworkConnector.ReportInfo> issueInfos) {
        this.reportInfos = issueInfos;
        // multimap file-to-issues generation for each report
        issuesList = new ArrayList<>();
        for (KlocworkConnector.ReportInfo info : issueInfos) {
            Report report = info.report;
            generateIssueAdapterList(info.config.getProjectPath(), report.getIssues());
        }
    }

    private String getFilepath(IssueAdapter i) {
        return i.getFilepath();
    }

    public List<IssueAdapter> getIssuesList() {
        return new ArrayList<>(issuesList);
    }

    public Multimap<String, IssueAdapter> asMultimap(Iterable<IssueAdapter> issues) {
        final Multimap<String, IssueAdapter> multimap = LinkedListMultimap.create();
        for (IssueAdapter i : issues) {
            multimap.put(getFilepath(i), i);
        }
        return multimap;
    }

    /**
     * Generates issues wrapper consisting corrected filepath
     */
    private void generateIssueAdapterList(String projectPath, Iterable<Issue> issues) {
        for (Issue issue : issues) {
            issuesList.add(new KlocworkIssueAdapter(issue, issue.getFile(), projectPath));
        }
    }

    @VisibleForTesting
    Report getRawReport(SubJobConfig config) {
        if (config != null && config.getProjectPath() != null && config.getKlocworkReportPath() != null) {
            for (KlocworkConnector.ReportInfo reportInfo : reportInfos) {
                SubJobConfig rconfig = reportInfo.config;
                if (config.getProjectPath().equals(rconfig.getProjectPath())
                        && config.getKlocworkReportPath().equals(rconfig.getKlocworkReportPath())) {
                    return reportInfo.report;
                }
            }
        }
        return null;
    }

}
