package org.jenkinsci.plugins.klocworkgerrit.inspection.entity;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class Issue {

    public Issue() {
    }

    public Issue(Issue issue) {
        this.id = issue.id;
        this.status = issue.status;
        this.severity = issue.severity;
        this.severityCode = issue.severityCode;
        this.state = issue.state;
        this.code = issue.code;
        this.title = issue.title;
        this.message = issue.message;
        this.file = issue.file;
        this.method = issue.method;
        this.owner = issue.owner;
        this.taxonomyName = issue.taxonomyName;
        this.dateOriginated = issue.dateOriginated;
        this.line = issue.line;
        this.url = issue.url;
        this.issueIds = issue.issueIds;
        this.trace = issue.trace;
    }
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setSeverityCode(Integer severityCode) {
        this.severityCode = severityCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTaxonomyName(String taxonomyName) {
        this.taxonomyName = taxonomyName;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

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

    public List<Integer> getIssueIds() {
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

