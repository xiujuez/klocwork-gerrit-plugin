package org.jenkinsci.plugins.klocworkgerrit.inspection;

import com.google.common.collect.Multimap;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

import java.util.Collection;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 10.01.2018 11:11
 */
public interface InspectionReportAdapter {
    Collection<IssueAdapter> getIssues();

    Multimap<String, IssueAdapter> getReportData();
}
