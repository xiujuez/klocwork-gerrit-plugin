package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import com.google.common.collect.Multimap;
import hudson.AbortException;
import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.klocworkgerrit.TaskListenerLogger;
import org.jenkinsci.plugins.klocworkgerrit.config.InspectionConfig;
import org.jenkinsci.plugins.klocworkgerrit.config.SubJobConfig;
import org.jenkinsci.plugins.klocworkgerrit.inspection.InspectionReportAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Report;
import org.jenkinsci.plugins.klocworkgerrit.util.Localization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.jenkinsci.plugins.klocworkgerrit.util.Localization.getLocalized;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 * <p>
 * $Id$
 */
public class KlocworkConnector implements InspectionReportAdapter {

    private static final Logger LOGGER = Logger.getLogger(KlocworkConnector.class.getName());

    private TaskListener listener;
    private InspectionReport report;
    private InspectionConfig inspectionConfig;

    public KlocworkConnector(TaskListener listener, InspectionConfig inspectionConfig) {
        this.inspectionConfig = inspectionConfig;
        this.listener = listener;
    }

    public void readKlocworkReports(FilePath workspace) throws IOException,
            InterruptedException {
        List<ReportInfo> reports = new ArrayList<ReportInfo>();
        for (SubJobConfig subJobConfig : inspectionConfig.getAllSubJobConfigs()) {
            Report report = readKlocworkReport(workspace, subJobConfig.getKlocworkReportPath());
            if (report == null) {  //todo fail all? skip errors?
                TaskListenerLogger.logMessage(listener, LOGGER, Level.SEVERE, "jenkins.plugin.error.path.no.project.config.available");
                throw new AbortException(getLocalized("jenkins.plugin.error.path.no.project.config.available"));
            }
            reports.add(new ReportInfo(subJobConfig, report));
        }
        report = new InspectionReport(reports);
    }

    public Multimap<String, IssueAdapter> getReportData() {
        return report.asMultimap(getIssues());
    }

    public Multimap<String, IssueAdapter> getReportData(Iterable<IssueAdapter> issues) {
        return report.asMultimap(issues);
    }

    public List<IssueAdapter> getIssues() {
        return report.getIssuesList();
    }

    Report getRawReport(SubJobConfig config) {
        return report.getRawReport(config);
    }

    private Report readKlocworkReport(FilePath workspace, String klocworkReportPath) throws IOException,
            InterruptedException {
        FilePath reportPath = workspace.child(klocworkReportPath);
        Report report;
        // check if report exists
        if (!reportPath.exists()) {
            TaskListenerLogger.logMessage(listener, LOGGER, Level.SEVERE, "jenkins.plugin.error.klocwork.report.not.exists", reportPath);
            return null;
        }
        // check if report is a file
        if (reportPath.isDirectory()) {
            TaskListenerLogger.logMessage(listener, LOGGER, Level.SEVERE, "jenkins.plugin.error.klocwork.report.path.directory", reportPath);
            return null;
        }

        TaskListenerLogger.logMessage(listener, LOGGER, Level.INFO, "jenkins.plugin.inspection.report.loading", reportPath);

        KlocworkReportBuilder builder = new KlocworkReportBuilder();
        report = builder.builder(reportPath);

        TaskListenerLogger.logMessage(listener, LOGGER, Level.INFO, "jenkins.plugin.inspection.report.loaded", report.getIssues().size());
        return report;
    }

    static class ReportInfo {

        public final SubJobConfig config;
        public final Report report;

        public ReportInfo(SubJobConfig config, Report report) {
            this.config = config;
            this.report = report;
        }

    }

}
