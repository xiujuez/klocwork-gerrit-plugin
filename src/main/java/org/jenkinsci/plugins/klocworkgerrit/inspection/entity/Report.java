package org.jenkinsci.plugins.klocworkgerrit.inspection.entity;



import java.util.ArrayList;
import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class Report  {

    private List<Issue> issues = new ArrayList<Issue>();

    public List<Issue> getIssues() {
        return issues;
    }

    public void insertIssue(Issue issue) {
        issues.add(issue);
    }

    @Override
    public String toString() {
        return "Report{" +
                "issues=" + issues +
                '}';
    }

}
