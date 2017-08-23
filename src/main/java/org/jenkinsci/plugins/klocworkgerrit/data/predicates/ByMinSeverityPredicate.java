package org.jenkinsci.plugins.klocworkgerrit.data.predicates;

import org.jenkinsci.plugins.klocworkgerrit.data.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.data.entity.Severity;
import com.google.common.base.Predicate;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByMinSeverityPredicate implements Predicate<Issue> {

    private final Severity severity;

    private ByMinSeverityPredicate(Severity severity) {
        this.severity = severity;
    }

    @Override
    public boolean apply(Issue issue) {
        return issue.getSeverity().equals(severity) || issue.getSeverity().ordinal() >= severity.ordinal();
    }

    public static ByMinSeverityPredicate apply(Severity severity) {
        return new ByMinSeverityPredicate(severity);
    }
}