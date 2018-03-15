package org.jenkinsci.plugins.klocworkgerrit.inspection.klocwork;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * $Id$
 */
public class KlocworkIssueAdapter extends Issue implements IssueAdapter {

    private String filepath;

    private String projectPath;

    public KlocworkIssueAdapter(Issue issue,  String filepath, String projectPath) {
        super(issue);
        this.filepath = filepath;
        this.projectPath = projectPath;
    }

    @Override
    public String getFilepath() {
        return "".equals(projectPath) ? filepath : filepath.replaceFirst(projectPath, "");
    }

    @Override
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
