package org.jenkinsci.plugins.klocworkgerrit.data.converter;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import org.jenkinsci.plugins.klocworkgerrit.data.predicates.ByExactSeverityPredicate;
import org.jenkinsci.plugins.klocworkgerrit.data.predicates.ByMinSeverityPredicate;
import org.jenkinsci.plugins.klocworkgerrit.util.Localization;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class CustomReportFormatter implements TagFormatter<CustomReportFormatter.Tag> {
    public static final String DEFAULT_NO_ISSUES_TEXT = Localization.getLocalized("jenkins.plugin.default.review.title.no.issues");
    public static final String DEFAULT_SOME_ISSUES_TEXT = Localization.getLocalized("jenkins.plugin.default.review.title.issues");

    private String successMessage;
    private String failMessage;

    private Iterable<Issue> issues;

    public CustomReportFormatter(Iterable<Issue> issues, String failMessage, String successMessage) {
        this.issues = issues;
        this.failMessage = prepareText(failMessage, DEFAULT_SOME_ISSUES_TEXT);
        this.successMessage = prepareText(successMessage, DEFAULT_NO_ISSUES_TEXT);
    }

    private static String prepareText(String text, String defaultValue) {
        return text != null && !text.trim().isEmpty() ? text.trim() : defaultValue;
    }

    @Override
    public String getMessage() {
        String res = getSize(issues) > 0 ? failMessage : successMessage;
        for (Tag tag : Tag.values()) {
            res = res.replace(tag.getName(), getValueToReplace(tag));
        }
        return res;
    }

    @Override
    public String getValueToReplace(Tag tag) {
        int value;
        switch (tag) {
            case REVIEW_ISSUE_COUNT:
            case WARNING_ISSUE_COUNT:
            case ERROR_ISSUE_COUNT:
            case CRITICAL_ISSUE_COUNT:
                value = getSize(filterByExactSeverityPredicate(tag.getSeverity()));
                return String.valueOf(value);
            case AT_LEAST_WARNING_ISSUE_COUNT:
            case AT_LEAST_ERROR_ISSUE_COUNT:
            case AT_LEAST_CRITICAL_ISSUE_COUNT:
            case TOTAL_COUNT:
                value = getSize(filterByMinSeverityPredicate(tag.getSeverity()));
                return String.valueOf(value);
            default:
                return "";
        }

    }

    private int getSize(Iterable i) {
        return Lists.newArrayList(i).size();
    }

    private Iterable<Issue> filterByExactSeverityPredicate(Severity s) {
        return Iterables.filter(issues, ByExactSeverityPredicate.apply(s));
    }

    private Iterable<Issue> filterByMinSeverityPredicate(Severity s) {
        return Iterables.filter(issues, ByMinSeverityPredicate.apply(s));
    }

    public enum Tag {
        REVIEW_ISSUE_COUNT("<review_count>", Severity.Review),
        WARNING_ISSUE_COUNT("<warning_count>", Severity.Warning),
        ERROR_ISSUE_COUNT("<error_count>", Severity.Error),
        CRITICAL_ISSUE_COUNT("<critical_count>", Severity.Critical),
        AT_LEAST_WARNING_ISSUE_COUNT("<min_warning_count>", Severity.Warning),
        AT_LEAST_ERROR_ISSUE_COUNT("<min_error_count>", Severity.Error),
        AT_LEAST_CRITICAL_ISSUE_COUNT("<min_critical_count>", Severity.Critical),
        TOTAL_COUNT("<total_count>", Severity.Review);

        private final String name;
        private final Severity severity;

        Tag(String name, Severity severity) {
            this.name = name;
            this.severity = severity;
        }

        public String getName() {
            return name;
        }

        public Severity getSeverity() {
            return severity;
        }
    }
}

