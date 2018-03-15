package org.jenkinsci.plugins.klocworkgerrit.filter.predicates;

import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Issue;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.klocworkgerrit.inspection.entity.Severity;
import com.google.common.base.Predicate;

/**
 * Project: Klocwork-Gerrit Plugin
 * Author:  Zheng XiuJue
 * Created: 21.08.2017 20:12
 *
 */
public class ByMinSeverityPredicate implements Predicate<IssueAdapter> {

    private final Severity severity;

    private ByMinSeverityPredicate(Severity severity) {
        this.severity = severity;
    }

    @Override
    public boolean apply(IssueAdapter issue) {
        return issue.getSeverity().equals(severity) || issue.getSeverity().ordinal() >= severity.ordinal();
    }

    public static ByMinSeverityPredicate apply(Severity severity) {
        return new ByMinSeverityPredicate(severity);
    }
}