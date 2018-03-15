package org.jenkinsci.plugins.klocworkgerrit.inspection.entity;


import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 28.12.2017 11:11
 * $Id$
 */
public interface IssueAdapter {

    String getFilepath();

    void setFilepath(String path);

    Integer getId();

    String getStatus();

    Severity getSeverity();

    Integer getSeverityCode();

    String getState();

    String getCode();

    String getTitle();

    String getMessage();

    String getFile();

    String getMethod();

    String getOwner();

    String getTaxonomyName();

    Long getDateOriginated();

    Integer getLine();

    String getUrl();

    List<Integer> getIssueIds();

    List<Trace> getTrace();

    boolean isNew();
}
