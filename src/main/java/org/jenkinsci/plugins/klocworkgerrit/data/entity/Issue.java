package org.jenkinsci.plugins.klocworkgerrit.data.entity;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class Issue {

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Integer id;

    @SuppressWarnings("unused")
    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    private String status;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Severity severity;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Integer severityCode;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String state;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String code;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String title;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String message;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String file;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String method;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String owner;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String taxonomyName;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Long dateOriginated;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private Integer line;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private String url;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private List<Integer> issueIds;

    @SuppressWarnings("unused")
    @SuppressFBWarnings ("UWF_UNWRITTEN_FIELD")
    private List<Trace> trace;

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Integer getSeverityCode() {
        return severityCode;
    }

    public String getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getFile() {
        return file;
    }

    public String getMethod() {
        return method;
    }

    public String getOwner() {
        return owner;
    }

    public String getTaxonomyName() {
        return taxonomyName;
    }

    public Long getDateOriginated() {
        return dateOriginated;
    }

    public Integer getLine() {
        return line;
    }

    public String getUrl() {
        return url;
    }

    private List<Integer> getIssueIds() {
        return issueIds;
    }

    public List<Trace> getTrace() {
        return trace;
    }

    public boolean isNew() {
        return state != null && state.equals("New");
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", severity='" + severity + '\'' +
                ", severityCode=" + severityCode +
                ", state='" + state + '\'' +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", file='" + file + '\'' +
                ", method='" + method + '\'' +
                ", owner='" + owner + '\'' +
                ", taxonomyName='" + taxonomyName + '\'' +
                ", dateOriginated=" + dateOriginated +
                ", line=" + line +
                ", url='" + url + '\'' +
                ", issueIds=" + issueIds +
                ", trace=" + trace +
                '}';
    }

}

