package org.jenkinsci.plugins.klocworkgerrit.integration;

import hudson.model.TaskListener;
import org.jenkinsci.plugins.klocworkgerrit.TaskListenerLogger;
import org.jenkinsci.plugins.klocworkgerrit.filter.predicates.ByFilenameEndPredicate;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.InspectionReportAdapter;
import org.jenkinsci.plugins.klocworkgerrit.review.RevisionAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 */
public class IssueAdapterProcessor {
    protected InspectionReportAdapter inspectionReport;
    protected RevisionAdapter revisionAdapter;
    protected Map<String, String> inspection2revisionFilepaths;
    private TaskListener listener;

    private static final Logger LOGGER = Logger.getLogger(IssueAdapterProcessor.class.getName());

    public IssueAdapterProcessor(TaskListener listener, InspectionReportAdapter inspectionReport, RevisionAdapter revisionAdapter) {
        this.listener = listener;
        this.inspectionReport = inspectionReport;
        this.revisionAdapter = revisionAdapter;
        this.inspection2revisionFilepaths = new HashMap<>();
    }

    public void process() {
        Iterable<IssueAdapter> issues = inspectionReport.getIssues();
        Set<String> changedFiles = revisionAdapter.getChangedFiles();
        for (IssueAdapter i : issues) {
            String reviewSystemFilePath = findReviewSystemFilepath(i, changedFiles);
            if (reviewSystemFilePath != null) {
                i.setFilepath(reviewSystemFilePath);
            }
        }
    }

    protected String findReviewSystemFilepath(IssueAdapter i, Set<String> files) {
        String filepath = i.getFilepath();
        if (inspection2revisionFilepaths.containsKey(filepath)) {
            return inspection2revisionFilepaths.get(filepath);
        }
        if (files.contains(filepath)) {
            inspection2revisionFilepaths.put(filepath, filepath);
            //return findReviewSystemFilepath(i, files); // extra if and extra get operations
            return filepath;
        }
        String found = null;
        for (String s : files) {
            if (namesMatch(i, s)) {
                if (found == null) {
                    found = s;
                } else {
                    // achtung! more than one match found!!
                    TaskListenerLogger.logMessage(listener, LOGGER, Level.SEVERE, "jenkins.plugin.error.more.than.one.file.matched", i.getFilepath());
                    return null;
                }
            }
        }
        return found;
    }

    protected boolean namesMatch(IssueAdapter issue, String reviewFilepath) {
        return ByFilenameEndPredicate.apply(reviewFilepath).apply(issue);
    }

}
