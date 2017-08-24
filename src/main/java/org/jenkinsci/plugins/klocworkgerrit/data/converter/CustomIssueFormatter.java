package org.jenkinsci.plugins.klocworkgerrit.data.converter;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Trace;
import org.jenkinsci.plugins.klocworkgerrit.util.Localization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class CustomIssueFormatter implements IssueFormatter, TagFormatter<CustomIssueFormatter.Tag> {

    public static final String DEFAULT_ISSUE_COMMENT_TEXT = Localization.getLocalized("jenkins.plugin.default.review.body");

    private Issue issue;
    private String text;
    private String host;

    public CustomIssueFormatter(Issue issue, String text, String host) {
        this.issue = issue;
        this.text = prepareText(text, DEFAULT_ISSUE_COMMENT_TEXT);
        this.host = host;
    }

    private static String prepareText(String text, String defaultValue) {
        return text != null && !text.trim().isEmpty() ? text.trim() : defaultValue;
    }

    @Override
    public String getMessage() {
        String res = text;
        for (Tag tag : Tag.values()) {
            if (res.contains(tag.getName())) {
                res = res.replace(tag.getName(), getValueToReplace(tag));
            }
        }
        return res;
    }

    @Override
    public String getValueToReplace(Tag tag) {
        switch (tag) {
            case ID:
                return issue.getId().toString();
            case FILE:
                return issue.getFile();
            case LINE:
                return issue.getLine().toString();
            case SEVERITY:
                return issue.getSeverity().name();
            case METHOD:
                return issue.getMethod();
            case TITLE:
                return issue.getTitle();
            case MESSAGE:
                return issue.getMessage();
            case RULE_URL:
                return getRuleLink(issue.getCode());
            default:
                return null;
        }
    }

    protected String getRuleLink(String rule) {
        if (host != null) {
            StringBuilder sb = new StringBuilder();
            String url = host.trim();
            if (!(url.startsWith("http://") || host.startsWith("https://"))) {
                sb.append("http://");
            }
            sb.append(url);
            if (!(url.endsWith("/"))) {
                sb.append("/");
            }
            sb.append("documentation/help/reference/");
            sb.append(rule.toLowerCase());
            sb.append(".htm?highlight=");
            sb.append(rule);
            return sb.toString();
        }
        return rule;
    }

    public enum Tag {
        ID("<id>"),
        FILE("<file>"),
        LINE("<line>"),
        SEVERITY("<severity>"),
        METHOD("<method>"),
        TITLE("<title>"),
        MESSAGE("<message>"),
        RULE_URL("<rule_url>");

        private final String name;

        Tag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

